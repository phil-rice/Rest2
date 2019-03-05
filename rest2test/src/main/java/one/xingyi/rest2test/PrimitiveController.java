package one.xingyi.rest2test;
import one.xingyi.core.client.ISimpleList;
import one.xingyi.core.store.ControllerUsingMap;
import one.xingyi.rest2test.server.controller.IPrimitivesController;
import one.xingyi.rest2test.server.domain.Primitives;

import java.util.List;
public class PrimitiveController extends ControllerUsingMap<Primitives> implements IPrimitivesController {
    public PrimitiveController() {
        super("Primitive");
        store.put("someId", prototype("someId"));
        store.put("prototype", prototype("prototype"));
    }
    @Override protected Primitives prototype(String id) { return new Primitives("name", 1, 2, false, true, 1.2, 2.2,
            ISimpleList.fromList(List.of("one")),ISimpleList.fromList(List.of(1)),
            ISimpleList.fromList(List.of(1.0)), ISimpleList.fromList(List.of(false))); }
    @Override public String stateFn(Primitives entity) { return null; }
}
