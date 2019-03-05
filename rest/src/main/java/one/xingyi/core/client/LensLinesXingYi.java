package one.xingyi.core.client;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.Getter;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.optics.Setter;
import one.xingyi.core.optics.lensLanguage.LensDefnStore;
import one.xingyi.core.optics.lensLanguage.LensStore;
import one.xingyi.core.sdk.IXingYiClientFactory;
import one.xingyi.core.sdk.IXingYiClientResource;
import one.xingyi.core.sdk.IXingYiView;
import one.xingyi.core.utils.Function3;
import one.xingyi.core.utils.IdAndValue;

import java.util.function.BiFunction;
import java.util.function.Function;

public class LensLinesXingYi<J, Entity extends IXingYiClientResource, View extends IXingYiView<Entity>> implements IXingYi<Entity, View> {
    final JsonParserAndWriter<J> parser;
    final LensStore<J> lensStore;
    public LensLinesXingYi(JsonParserAndWriter<J> parser, LensDefnStore lensStore) {
        this.parser = parser;
        this.lensStore = lensStore.makeStore(parser);
    }
    @Override public Object parse(String s) { return parser.parse(s); }

    @Override public Lens<View, String> stringLens(IXingYiClientFactory<Entity, View> maker, String name) {
        Lens<J, String> lens = lensStore.stringLens(name);
        return viewToMirrorL(maker).andThen(Lens.<Object, J>cast()).andThen(lens);
    }
    @Override public Lens<View, Double> doubleLens(IXingYiClientFactory<Entity, View> maker, String name) {
        Lens<J, Double> lens = lensStore.doubleLens(name);
        return viewToMirrorL(maker).andThen(Lens.<Object, J>cast()).andThen(lens);
    }
    @Override public Lens<View, Integer> integerLens(IXingYiClientFactory<Entity, View> maker, String name) {
        Lens<J, Integer> lens = lensStore.integerLens(name);
        return viewToMirrorL(maker).andThen(Lens.<Object, J>cast()).andThen(lens);
    }
    @Override public Lens<View, Boolean> booleanLens(IXingYiClientFactory<Entity, View> maker, String name) {
        Lens<J, Boolean> lens = lensStore.booleanLens(name);
        return viewToMirrorL(maker).andThen(Lens.<Object, J>cast()).andThen(lens);
    }

    @Override
    public <ChildEntity extends IXingYiClientResource, ChildView extends IXingYiView<ChildEntity>> Lens<View, ChildView> objectLens(IXingYiClientFactory<Entity, View> maker, IXingYiClientFactory<ChildEntity, ChildView> childMaker, String name) {
        return viewToMirrorL(maker).andThen(Lens.<Object, J>cast()).andThen(parser.lensToChild(name)).andThen(Lens.<J, Object>cast()).andThen(mirrorToViewL(childMaker));
    }
    @Override public IdAndValue getIdAndValue(Object mirror, IXingYiClientFactory<Entity, View> maker) {
        return new IdAndValue<>(this.parser.asString((J) mirror, "id"), maker.make(this, this.parser.child((J) mirror, "value")));
    }
    @Override
    public <ChildEntity extends IXingYiClientResource, ChildView extends IXingYiView<ChildEntity>> Lens<View, IResourceList<ChildView>> listLens(IXingYiClientFactory<Entity, View> maker, IXingYiClientFactory<ChildEntity, ChildView> childMaker, String name) {
        throw new RuntimeException("don't do lists yet");
    }
    @Override public <ChildEntity extends IXingYiClientResource, ChildView extends IXingYiView<ChildEntity>> String render(String renderName, View view) {
        if (renderName.equalsIgnoreCase("json")) return parser.fromJ((J) view.mirror());
        throw new RuntimeException("Unrecognised renderName" + renderName + " only legal value is 'json'");
    }

    @Override public Lens<View, ISimpleList<String>> simpleStringListLens(IXingYiClientFactory<Entity, View> maker, String name) {
        return simpleListLens(maker, name, (p, j, n) -> p.asSimpleStringList(j, n));
    }
    @Override public Lens<View, ISimpleList<Integer>> simpleIntegerListLens(IXingYiClientFactory<Entity, View> maker, String name) {
        return simpleListLens(maker, name, (p, j, n) -> p.asSimpleIntegerList(j, n));
    }
    @Override public Lens<View, ISimpleList<Double>> simpleDoubleListLens(IXingYiClientFactory<Entity, View> maker, String name) {
        return simpleListLens(maker, name, (p, j, n) -> p.asSimpleDoubleList(j, n));
    }
    @Override public Lens<View, ISimpleList<Boolean>> simpleBooleanListLens(IXingYiClientFactory<Entity, View> maker, String name) {
        return simpleListLens(maker, name, (p, j, n) -> p.asSimpleBooleanList(j, n));
    }
    <T> Lens<View, ISimpleList<T>> simpleListLens(IXingYiClientFactory<Entity, View> maker, String name, Function3<JsonParserAndWriter<J>, J, String, ISimpleList<T>> fn) {
        Getter<View, ISimpleList<T>> getter = v -> fn.apply(parser, (J) v.mirror(), name);
        Setter<View, ISimpleList<T>> setter = (v, l) -> maker.make(this, parser.makeSimpleList(l));
        return Lens.<View, ISimpleList<T>>create(getter, setter);
    }
    static <Entity extends IXingYiClientResource, View extends IXingYiView<Entity>> Lens<View, Object> viewToMirrorL(IXingYiClientFactory<Entity, View> maker) {
        return Lens.create(View::mirror, (v, m) -> maker.make(v.xingYi(), m));
    }
    <OtherEntity extends IXingYiClientResource, OtherView extends IXingYiView<OtherEntity>> Lens<Object, OtherView> mirrorToViewL(IXingYiClientFactory<OtherEntity, OtherView> maker) {
        return Lens.create(m -> maker.make(this, m), (m, v) -> v.mirror());
    }

}
