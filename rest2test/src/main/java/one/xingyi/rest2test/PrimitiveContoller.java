package one.xingyi.rest2test;
import one.xingyi.core.store.ControllerUsingMap;
import one.xingyi.rest2test.server.controller.IPrimitivesController;
import one.xingyi.rest2test.server.domain.Primitives;
public class PrimitiveContoller extends ControllerUsingMap<Primitives> implements IPrimitivesController {
    public PrimitiveContoller() {
        super("Primitive");
        store.put("someId", prototype("someId"));
        store.put("prototype", prototype("prototype"));
    }
    @Override protected Primitives prototype(String id) { return new Primitives("name", 1, 2, false, true, 1.2, 2.2); }
    @Override public String stateFn(Primitives entity) { return null; }
}
