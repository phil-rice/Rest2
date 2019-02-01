package one.xingyi.trafficlights;
import one.xingyi.core.annotations.*;
import one.xingyi.core.sdk.IXingYiView;
import one.xingyi.core.sdk.IXingYiViewDefn;

@View
public interface IColourViewDefn extends IXingYiViewDefn<ITrafficLightsDefn> {
//    @Field(readOnly = true)
    String id();

    String color();

}
