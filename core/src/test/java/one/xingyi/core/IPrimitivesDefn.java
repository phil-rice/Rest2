package one.xingyi.core;
import one.xingyi.core.annotations.Resource;
import one.xingyi.core.sdk.IXingYiResourceDefn;

@Resource
public interface IPrimitivesDefn extends IXingYiResourceDefn {
    String name();
    Integer integerBoxed();
    int integer();
    Boolean booleanBoxed();
    boolean bool();
    Double doubleBoxed();
    double doub();
}


