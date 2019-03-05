package one.xingyi.core.primitives;
import one.xingyi.core.sdk.IXingYiRemoteAccessDetails;
import one.xingyi.rest2test.PrimitiveController;
import one.xingyi.rest2test.PrimitiveServer;
import one.xingyi.rest2test.client.entitydefn.IPrimitivesClientEntity;
import one.xingyi.rest2test.client.view.PrimitiveView;
import one.xingyi.rest2test.client.viewcompanion.PrimitiveViewCompanion;
import one.xingyi.rest2test.server.domain.IPrimitives;
public abstract class AbstractPrimitiveSimpleListTest<T> extends AbstractSimpleListTest<IPrimitives, IPrimitivesClientEntity, PrimitiveView, PrimitiveServer<Object>, T> {
    @Override protected PrimitiveServer<Object> server() { return new PrimitiveServer<Object>(config, new PrimitiveController()); }
    @Override protected IXingYiRemoteAccessDetails<IPrimitivesClientEntity, PrimitiveView> accessDetails() { return PrimitiveViewCompanion.companion; }
}
