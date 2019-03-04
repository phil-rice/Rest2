package one.xingyi.core.primitives;
import one.xingyi.core.AbstractResourceTest;
import one.xingyi.core.sdk.IXingYiRemoteAccessDetails;
import one.xingyi.rest2test.PrimitiveContoller;
import one.xingyi.rest2test.PrimitiveServer;
import one.xingyi.rest2test.client.entitydefn.IPrimitivesClientEntity;
import one.xingyi.rest2test.client.view.PrimitiveView;
import one.xingyi.rest2test.client.viewcompanion.PrimitiveViewCompanion;
import one.xingyi.rest2test.server.companion.PrimitivesCompanion;
import one.xingyi.rest2test.server.domain.IPrimitives;
public abstract class AbstractPrimitivesTest<T> extends AbstractResourceTest<IPrimitives, IPrimitivesClientEntity, PrimitiveView, PrimitiveServer<Object>, T> {
    @Override protected PrimitiveServer<Object> server() {
        return new PrimitiveServer<>(config, new PrimitiveContoller());
    }
    @Override protected IXingYiRemoteAccessDetails<IPrimitivesClientEntity, PrimitiveView> accessDetails() {
        return PrimitiveViewCompanion.companion;
    }

}
