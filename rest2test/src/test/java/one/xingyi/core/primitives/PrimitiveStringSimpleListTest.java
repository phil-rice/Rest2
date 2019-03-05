package one.xingyi.core.primitives;
import one.xingyi.core.client.ISimpleList;
import one.xingyi.rest2test.client.view.PrimitiveView;
public class PrimitiveStringSimpleListTest extends AbstractPrimitiveSimpleListTest<String> {
    @Override protected ISimpleList<String> getItem(PrimitiveView view) { return view.stringList(); }
    @Override protected PrimitiveView withItem(PrimitiveView view, ISimpleList<String> item) { return view.withstringList(item); }
    @Override protected String item1() {
        return "one";
    }
    @Override protected String item2() {
        return "two";
    }
    @Override protected String item3() {
        return "three";
    }
}
