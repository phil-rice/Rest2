package one.xingyi.core.primitives;
import one.xingyi.rest2test.client.view.PrimitiveView;
public class BoxedIntegerPrimitiveTest extends AbstractPrimitivesTest<Integer> {
    @Override protected Integer getItem(PrimitiveView view) {
        return view.integerBoxed();
    }
    @Override protected PrimitiveView withItem(PrimitiveView view, Integer item) {
        return view.withintegerBoxed(item);
    }
    @Override protected Integer startItem() {
        return 1;
    }
    @Override protected Integer secondItem() { return 2; }
}
