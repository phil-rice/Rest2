package one.xingyi.core.primitives;
import one.xingyi.rest2test.client.view.PrimitiveView;
public class BooleanPrimitiveTest extends AbstractPrimitivesTest<Boolean> {
    @Override protected Boolean getItem(PrimitiveView view) {
        return view.bool();
    }
    @Override protected PrimitiveView withItem(PrimitiveView view, Boolean item) {
        return view.withbool(item);
    }
    @Override protected Boolean startItem() {
        return true;
    }
    @Override protected Boolean secondItem() {
        return false;
    }
}
