package one.xingyi.trafficlights;
import one.xingyi.core.annotations.Field;
import one.xingyi.core.annotations.View;
import one.xingyi.core.sdk.IXingYiViewDefn;

@View
public interface ILocationViewDefn extends IXingYiViewDefn<ITrafficLightsDefn> {
    String location();

}
