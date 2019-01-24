package one.xingyi.trafficlights;
import one.xingyi.core.annotations.Entity;
import one.xingyi.core.annotations.Field;

import one.xingyi.core.annotations.Post;
import one.xingyi.core.sdk.IXingYiEntityDefn;
import one.xingyi.trafficlights.domain.TrafficLights;


@Entity(bookmark = "/lights", getUrl = "{host}/light/{id}")
public interface ITrafficLightsDefn extends IXingYiEntityDefn {
    @Field(readOnly = true)
    String id();
    String color();


}