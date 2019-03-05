package one.xingyi.core.primitives;
import one.xingyi.core.client.ISimpleList;
import one.xingyi.rest2test.client.view.PrimitiveView;
public class PrimitiveBooleanSimpleListTest extends AbstractPrimitiveSimpleListTest<Boolean> {
    @Override protected ISimpleList<Boolean> getItem(PrimitiveView view) { return view.booleanList(); }
    @Override protected PrimitiveView withItem(PrimitiveView view, ISimpleList<Boolean> item) { return view.withbooleanList(item); }
    @Override protected Boolean item1() {
        return false;
    }
    @Override protected Boolean item2() {
        return true;
    }
    @Override protected Boolean item3() {
        return false;
    }
}
