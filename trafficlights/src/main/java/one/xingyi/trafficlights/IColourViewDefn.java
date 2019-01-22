package one.xingyi.trafficlights;
import one.xingyi.core.annotations.View;
import one.xingyi.core.sdk.IXingYiView;
import one.xingyi.core.sdk.IXingYiViewDefn;
@View
public interface IColourViewDefn extends IXingYiViewDefn<ITrafficLightsDefn> {
    String id();
    String color();
}
