package one.xingyi.core.client;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.optics.lensLanguage.LensDefnStore;
import one.xingyi.core.sdk.IXingYiClientFactory;
import one.xingyi.core.sdk.IXingYiClientResource;
import one.xingyi.core.sdk.IXingYiView;
import one.xingyi.core.utils.IdAndValue;
@RequiredArgsConstructor
public class LensLinesXingYi<Entity extends IXingYiClientResource, View extends IXingYiView<Entity>> implements IXingYi<Entity, View> {
    final JsonParserAndWriter<Object> json;
    final LensDefnStore lensDefnStore;


    static <Entity extends IXingYiClientResource, View extends IXingYiView<Entity>> Lens<View, Object> viewToMirrorL(IXingYiClientFactory<Entity, View> maker) {
        return Lens.create(View::mirror, (v, m) -> maker.make(v.xingYi(), m));
    }
    <OtherEntity extends IXingYiClientResource, OtherView extends IXingYiView<OtherEntity>> Lens<Object, OtherView> mirrorToViewL(IXingYiClientFactory<OtherEntity, OtherView> maker) {
        return Lens.create(m -> maker.make(this, m), (m, v) -> v.mirror());
    }
    <OtherEntity extends IXingYiClientResource, OtherView extends IXingYiView<OtherEntity>>
    Lens<Object, IResourceList<OtherView>> mirrorToListViewL(IXingYiClientFactory<OtherEntity, OtherView> maker) {
        throw new RuntimeException("Don't do lists yet");
    }

    @Override public Object parse(String s) { return json.parse(s); }
    @Override public Lens<View, String> stringLens(IXingYiClientFactory<Entity, View> maker, String name) {
        return viewToMirrorL(maker).andThen(lensDefnStore.stringLens(name));
    }
    @Override
    public <ChildEntity extends IXingYiClientResource, ChildView extends IXingYiView<ChildEntity>> Lens<View, ChildView> objectLens(IXingYiClientFactory<Entity, View> maker, IXingYiClientFactory<ChildEntity, ChildView> childMaker, String name) {
        return viewToMirrorL(maker).andThen(json.lensToChild(name)).andThen(mirrorToViewL(childMaker));
    }
    @Override public IdAndValue getIdAndValue(Object mirror, IXingYiClientFactory<Entity, View> maker) {
        return new IdAndValue<>(json.asString(mirror, "id"), maker.make(this, json.child(mirror, "value")));
    }
    @Override
    public <ChildEntity extends IXingYiClientResource, ChildView extends IXingYiView<ChildEntity>> Lens<View, IResourceList<ChildView>> listLens(IXingYiClientFactory<Entity, View> maker, IXingYiClientFactory<ChildEntity, ChildView> childMaker, String name) {
        throw new RuntimeException("don't do lists yet");
    }
    @Override public <ChildEntity extends IXingYiClientResource, ChildView extends IXingYiView<ChildEntity>> String render(String renderName, View view) {
        if (renderName.equalsIgnoreCase("parserAndWriter")) return json.fromJ(view.mirror());
        throw new RuntimeException("Unrecognised renderName" + renderName + " only legal value is 'parserAndWriter'");
    }
}
