package one.xingyi.core.primitives;
import one.xingyi.rest2test.client.view.PrimitiveView;
public class DoublePrimitiveTest extends AbstractPrimitivesTest<Double> {
    @Override protected Double getItem(PrimitiveView view) {
        return view.doub();
    }
    @Override protected PrimitiveView withItem(PrimitiveView view, Double item) { return view.withdoub(item); }
    @Override protected Double startItem() {
        return 2.2;
    }
    @Override protected Double secondItem() { return 5.5; }
}
