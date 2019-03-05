package one.xingyi.rest2test;
import one.xingyi.core.annotations.*;
import one.xingyi.core.client.ISimpleList;
import one.xingyi.core.sdk.IXingYiResourceDefn;

@OptionalGet
@Get
@Delete
@Put
@CreateWithoutId
@Create
@PrototypeNoId(prototypeId = "prototype")
@Resource(bookmark = "/primitive", rootUrl = "{host}/primitive", urlWithId = "{host}/primitive/{id}")
public interface IPrimitivesDefn extends IXingYiResourceDefn {
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


