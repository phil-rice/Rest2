package one.xingyi.core.primitives;
import one.xingyi.rest2test.client.view.PrimitiveView;
public class BooleanBoxedPrimitiveTest extends AbstractPrimitivesTest<Boolean> {
    @Override protected Boolean getItem(PrimitiveView view) { return view.booleanBoxed(); }
    @Override protected PrimitiveView withItem(PrimitiveView view, Boolean item) {
        return view.withbooleanBoxed(item);
    }
    @Override protected Boolean startItem() {
        return false;
    }
    @Override protected Boolean secondItem() {
        return true;
    }
}
