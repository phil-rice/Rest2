package one.xingyi.core.primitives;
import one.xingyi.core.client.ISimpleList;
import one.xingyi.rest2test.client.view.PrimitiveView;
public class PrimitiveIntegerSimpleListTest extends AbstractPrimitiveSimpleListTest<Integer> {
    @Override protected ISimpleList<Integer> getItem(PrimitiveView view) { return view.integerList(); }
    @Override protected PrimitiveView withItem(PrimitiveView view, ISimpleList<Integer> item) { return view.withintegerList(item); }
    @Override protected Integer item1() {
        return 1;
    }
    @Override protected Integer item2() {
        return 2;
    }
    @Override protected Integer item3() {
        return 2;
    }
}
