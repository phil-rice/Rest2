package one.xingyi.trafficlights;
import one.xingyi.core.annotations.Entity;
import one.xingyi.core.annotations.Field;
import one.xingyi.core.sdk.IXingYiEntityDefn;
import one.xingyi.trafficlights.domain.TrafficLights;
import one.xingyi.trafficlights.operations.StateCalculator;

@Entity(bookmark = "/lights", getUrl = "<host>/light/<id>")
public interface ITrafficLightsDefn extends IXingYiEntityDefn {
    @State //This can be detected by the annotation processor. It is optional
//    StateCalculator<TrafficLights, String> calculator = tl -> tl.color();

    @Field(readOnly = true)
    String id();
    String color();


}
