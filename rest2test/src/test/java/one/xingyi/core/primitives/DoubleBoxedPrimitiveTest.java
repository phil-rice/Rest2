package one.xingyi.core.primitives;
import one.xingyi.rest2test.client.view.PrimitiveView;
public class DoubleBoxedPrimitiveTest extends AbstractPrimitivesTest<Double> {
    @Override protected Double getItem(PrimitiveView view) {
        return view.doubleBoxed();
    }
    @Override protected PrimitiveView withItem(PrimitiveView view, Double item) { return view.withdoubleBoxed(item); }
    @Override protected Double startItem() {
        return 1.2;
    }
    @Override protected Double secondItem() { return 2.2; }
}
