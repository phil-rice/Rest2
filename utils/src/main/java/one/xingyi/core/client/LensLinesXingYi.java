package one.xingyi.core.client;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.optics.lensLanguage.LensDefnStore;
import one.xingyi.core.optics.lensLanguage.LensStore;
import one.xingyi.core.sdk.IXingYiClientFactory;
import one.xingyi.core.sdk.IXingYiClientResource;
import one.xingyi.core.sdk.IXingYiView;
import one.xingyi.core.utils.IdAndValue;

public class LensLinesXingYi<Entity extends IXingYiClientResource, View extends IXingYiView<Entity>> implements IXingYi<Entity, View> {
    final JsonParserAndWriter<Object> parser;
    final LensStore<Object> lensStore;
    public LensLinesXingYi(JsonParserAndWriter<Object> parser, LensDefnStore lensStore) {
        this.parser = parser;
        this.lensStore = lensStore.makeStore(parser);
    }
    @Override public Object parse(String s) { return parser.parse(s); }

    @Override public Lens<View, String> stringLens(IXingYiClientFactory<Entity, View> maker, String name) {
        return viewToMirrorL(maker).andThen(lensStore.stringLens(name));
    }

    @Override
    public <ChildEntity extends IXingYiClientResource, ChildView extends IXingYiView<ChildEntity>> Lens<View, ChildView> objectLens(IXingYiClientFactory<Entity, View> maker, IXingYiClientFactory<ChildEntity, ChildView> childMaker, String name) {
        return viewToMirrorL(maker).andThen(parser.lensToChild(name)).andThen(mirrorToViewL(childMaker));
    }
    @Override public IdAndValue getIdAndValue(Object mirror, IXingYiClientFactory<Entity, View> maker) {
        return new IdAndValue<>(this.parser.asString(mirror, "id"), maker.make(this, this.parser.child(mirror, "value")));
    }
    @Override
    public <ChildEntity extends IXingYiClientResource, ChildView extends IXingYiView<ChildEntity>> Lens<View, IResourceList<ChildView>> listLens(IXingYiClientFactory<Entity, View> maker, IXingYiClientFactory<ChildEntity, ChildView> childMaker, String name) {
        throw new RuntimeException("don't do lists yet");
    }
    @Override public <ChildEntity extends IXingYiClientResource, ChildView extends IXingYiView<ChildEntity>> String render(String renderName, View view) {
        if (renderName.equalsIgnoreCase("json")) return parser.fromJ(view.mirror());
        throw new RuntimeException("Unrecognised renderName" + renderName + " only legal value is 'parserAndWriter'");
    }

    static <Entity extends IXingYiClientResource, View extends IXingYiView<Entity>> Lens<View, Object> viewToMirrorL(IXingYiClientFactory<Entity, View> maker) {
        return Lens.create(View::mirror, (v, m) -> maker.make(v.xingYi(), m));
    }
    <OtherEntity extends IXingYiClientResource, OtherView extends IXingYiView<OtherEntity>> Lens<Object, OtherView> mirrorToViewL(IXingYiClientFactory<OtherEntity, OtherView> maker) {
        return Lens.create(m -> maker.make(this, m), (m, v) -> v.mirror());
    }

}
