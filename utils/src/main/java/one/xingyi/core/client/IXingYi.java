package one.xingyi.core.client;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import one.xingyi.core.optics.Getter;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.optics.Setter;
import one.xingyi.core.sdk.*;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import java.util.concurrent.Callable;
public interface IXingYi<Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>> {
    Object parse(String s);
    Lens<View, String> stringLens(IXingYiClientFactory<Entity, View> maker, String name);

    <ChildEntity extends IXingYiClientEntity, ChildView extends IXingYiView<ChildEntity>> Lens<View, ChildView>
    objectLens(IXingYiClientFactory<Entity, View> maker,IXingYiClientFactory<ChildEntity, ChildView> childMaker, String name);


}


class XingYiExecutionException extends RuntimeException {
    XingYiExecutionException(String s, Exception e) {super(s, e);}

    static <T> T wrap(String message, Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new XingYiExecutionException("Error executing " + message, e);
        }
    }
}

class DefaultXingYi<Entity extends IXingYiClientEntity, View extends IXingYiView<Entity>> implements IXingYi<Entity, View> {

    final ScriptEngine engine;
    final Invocable inv;

    DefaultXingYi(String javaScript) {
        long time = System.nanoTime();
        engine = new NashornScriptEngineFactory().getScriptEngine("--language=es6 ");
        XingYiExecutionException.wrap("initialising", () -> engine.eval(javaScript));
        this.inv = (Invocable) engine;
        System.out.println("Duration: " + (System.nanoTime() - time) / 1000000);
    }
    @Override public Object parse(String s) { return XingYiExecutionException.wrap("parse. Strings was \n"+ s, () -> inv.invokeFunction("parse", s)); }

    @Override public Lens<View, String> stringLens(IXingYiClientFactory<Entity, View> maker, String name) {
        Getter<View, String> getter = t -> XingYiExecutionException.wrap("stringLens.getEntity" + name, () -> (String) inv.invokeFunction("getL", name, t.mirror()));
        Setter<View, String> setter = (t, s) -> XingYiExecutionException.wrap("stringLens.set" + name, () -> maker.create(this, inv.invokeFunction("setL", name, t.mirror(), s)));
        return Lens.create(getter, setter);
    }
    @Override public <ChildEntity extends IXingYiClientEntity, ChildView extends IXingYiView<ChildEntity>> Lens<View, ChildView> objectLens(IXingYiClientFactory<Entity, View> maker, IXingYiClientFactory<ChildEntity, ChildView> maker2, String name) {
        Getter<View, ChildView> getter = t -> XingYiExecutionException.<ChildView>wrap("objectLens.getEntity" + name, () -> maker2.create(this, inv.invokeFunction("getL", name, t.mirror())));
        Setter<View, ChildView> setter = (t, s) -> XingYiExecutionException.<View>wrap("objectLens.set" + name, () -> maker.create(this, inv.invokeFunction("setL", name, t.mirror(), s)));
        return Lens.create(getter, setter);
    }
    //    }
//    @Override public <View extends XingYiDomain, ChildView> Lens<View, ChildView> objectLens(IDomainMaker<View> domainMaker1, IDomainMaker<ChildView> domainMaker2, String name) {
//    }
}
