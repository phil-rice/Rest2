package one.xingyi.rest2test;
import one.xingyi.core.annotations.View;
import one.xingyi.core.client.ISimpleList;
import one.xingyi.core.sdk.IXingYiViewDefn;
import one.xingyi.rest2test.IPrimitivesDefn;

@View
public interface IPrimitiveViewDefn extends IXingYiViewDefn<IPrimitivesDefn> {
    String name();
    Integer integerBoxed();
    int integer();
    Boolean booleanBoxed();
    boolean bool();
    Double doubleBoxed();
    double doub();
    ISimpleList<String> stringList();
    ISimpleList<Integer> integerList();
    ISimpleList<Double> doubleList();
    ISimpleList<Boolean> booleanList();
}
