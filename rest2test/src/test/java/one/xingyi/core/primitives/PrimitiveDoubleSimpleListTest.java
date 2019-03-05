package one.xingyi.core.primitives;
import one.xingyi.core.client.ISimpleList;
import one.xingyi.rest2test.client.view.PrimitiveView;
public class PrimitiveDoubleSimpleListTest extends AbstractPrimitiveSimpleListTest<Double> {
    @Override protected ISimpleList<Double> getItem(PrimitiveView view) { return view.doubleList(); }
    @Override protected PrimitiveView withItem(PrimitiveView view, ISimpleList<Double> item) { return view.withdoubleList(item); }
    @Override protected Double item1() {
        return 1.0;
    }
    @Override protected Double item2() {
        return 2.1;
    }
    @Override protected Double item3() {
        return 2.3;
    }
}
