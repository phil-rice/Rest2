package one.xingyi.core.client;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.Getter;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.optics.Setter;
import one.xingyi.core.sdk.*;
import one.xingyi.core.utils.IdAndValue;
import one.xingyi.core.utils.WrappedException;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import java.util.concurrent.Callable;

//I tried to make the object a generic 'mirror' but the impact was enormous, and there seemed to be little win
public interface IXingYi<Entity extends IXingYiClientResource, View extends IXingYiView<Entity>> {
    Object parse(String s);
    Lens<View, String> stringLens(IXingYiClientFactory<Entity, View> maker, String name);
    IdAndValue getIdAndValue(Object mirror, IXingYiClientFactory<Entity, View> maker); //TODO Dog food this in the same way that we did with UrlPattern

    <ChildEntity extends IXingYiClientResource, ChildView extends IXingYiView<ChildEntity>> Lens<View, ChildView>
    objectLens(IXingYiClientFactory<Entity, View> maker, IXingYiClientFactory<ChildEntity, ChildView> childMaker, String name);

    <ChildEntity extends IXingYiClientResource, ChildView extends IXingYiView<ChildEntity>>
    Lens<View, IResourceList<ChildView>> listLens(IXingYiClientFactory<Entity, View> maker, IXingYiClientFactory<ChildEntity, ChildView> childMaker, String name);

    <ChildEntity extends IXingYiClientResource, ChildView extends IXingYiView<ChildEntity>> String render(String renderName, View view);
}


class XingYiExecutionException extends RuntimeException {
    XingYiExecutionException(String s, Exception e) {super(s, e);}

    static <T> T wrap(String message, String javascript, Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new XingYiExecutionException("Error executing " + message + "\nJavascript was\n" + javascript, e);
        }
    }
}

class DefaultXingYi<Entity extends IXingYiClientResource, View extends IXingYiView<Entity>> implements IXingYi<Entity, View> {

    final ScriptEngine engine;
    final Invocable inv;
    final String javaScript;

    DefaultXingYi(String javaScript) {
        this.javaScript = javaScript;
        long time = System.nanoTime();
        engine = new NashornScriptEngineFactory().getScriptEngine("--language=es6 ");
        XingYiExecutionException.wrap("initialising", javaScript, () -> engine.eval(javaScript));
        this.inv = (Invocable) engine;
//        System.out.println("Duration: " + (System.nanoTime() - time) / 1000000);
    }
    @Override public <ChildEntity extends IXingYiClientResource, ChildView extends IXingYiView<ChildEntity>> String render(String renderName, View view) {
        return rawRender(renderName, view.mirror());
    }
    String rawRender(String name, Object mirror) {
        return XingYiExecutionException.wrap("render", javaScript, () -> inv.invokeFunction("render_" + name, mirror).toString());
    }
    @Override public Object parse(String s) { return XingYiExecutionException.wrap("parse. Strings was \n" + s, javaScript, () -> inv.invokeFunction("parse", s)); }

    @Override public Lens<View, String> stringLens(IXingYiClientFactory<Entity, View> maker, String name) {
        Getter<View, String> getter = t -> XingYiExecutionException.wrap("stringLens.getEntity " + name, javaScript, () -> (String) inv.invokeFunction("getL", name, t.mirror()));
        Setter<View, String> setter = (t, s) -> XingYiExecutionException.wrap("stringLens.set" + name, javaScript, () -> maker.make(this, inv.invokeFunction("setL", name, t.mirror(), s)));
        return Lens.create(getter, setter);
    }
    @Override public IdAndValue getIdAndValue(Object mirror, IXingYiClientFactory<Entity, View> maker) {
        return XingYiExecutionException.wrap("getIdAndValue", javaScript, () -> {
            String id = (String) inv.invokeFunction("getField", mirror, "id");
            Object valueMirror = inv.invokeFunction("getField", mirror, "value");
            return new IdAndValue<>(id, maker.make(this, valueMirror));
        });
    }
    @Override
    public <ChildEntity extends IXingYiClientResource, ChildView extends IXingYiView<ChildEntity>> Lens<View, ChildView> objectLens(IXingYiClientFactory<Entity, View> maker, IXingYiClientFactory<ChildEntity, ChildView> maker2, String name) {
        Getter<View, ChildView> getter = t -> XingYiExecutionException.<ChildView>wrap("objectLens.getEntity" + name, javaScript, () -> maker2.make(this, inv.invokeFunction("getL", name, t.mirror())));
        Setter<View, ChildView> setter = (t, s) -> XingYiExecutionException.<View>wrap("objectLens.set" + name, javaScript, () -> maker.make(this, inv.invokeFunction("setL", name, t.mirror(), s)));
        return Lens.create(getter, setter);
    }

    @Override
    public <ChildEntity extends IXingYiClientResource, ChildView extends IXingYiView<ChildEntity>> Lens<View, IResourceList<ChildView>> listLens(IXingYiClientFactory<Entity, View> maker, IXingYiClientFactory<ChildEntity, ChildView> childMaker, String name) {
        Getter<View, IResourceList<ChildView>> getter = t ->
                XingYiExecutionException.<IResourceList<ChildView>>wrap("listLens.getEntity" + name, javaScript,
                        () -> {
                            ScriptObjectMirror mirror = ((ScriptObjectMirror) inv.invokeFunction("getL", name, t.mirror()));
                            return new MirroredResourceList<ChildView>(mirror,
                                    o -> childMaker.make(this, o),
                                    (main, n, child) -> (ScriptObjectMirror) WrappedException.wrapCallable(() -> inv.invokeFunction("setInList", main, n, child.mirror())),
                                    (main, child) -> (ScriptObjectMirror) WrappedException.wrapCallable(() -> inv.invokeFunction("addToList", main, child.mirror()))
                            );
                        });
        Setter<View, IResourceList<ChildView>> setter =
                (t, s) -> XingYiExecutionException.<View>wrap("listLens.set" + name, javaScript,
                        () -> maker.make(this, inv.invokeFunction("setL", name, t.mirror(), ((MirroredResourceList<ChildView>) s).mirror)));
        return Lens.<View, IResourceList<ChildView>>create(getter, setter);
    }
//    @Override public <View extends XingYiDomain, ChildView> Lens<View, ChildView> objectLens(IDomainMaker<View> domainMaker1, IDomainMaker<ChildView> domainMaker2, String name) {
//    }
}
