package one.xingyi.core.primitives;
import one.xingyi.rest2test.client.view.PrimitiveView;
public class IntegerPrimitiveTest extends AbstractPrimitivesTest<Integer> {
    @Override protected Integer getItem(PrimitiveView view) {
        return view.integer();
    }
    @Override protected PrimitiveView withItem(PrimitiveView view, Integer item) {
        return view.withinteger(item);
    }
    @Override protected Integer startItem() {
        return 2;
    }
    @Override protected Integer secondItem() {
        return 34;
    }
}
