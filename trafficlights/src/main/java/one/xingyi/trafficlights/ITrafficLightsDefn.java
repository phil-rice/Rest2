package one.xingyi.trafficlights;
import one.xingyi.core.annotations.*;
import one.xingyi.core.sdk.IXingYiEntityDefn;


@Entity(bookmark = "/lights", rootUrl = "{host}/lights/{id}")
@Delete
@Get(mustExist = false)
@Put
@CreateWithoutId(url = "{host}/lights")
@Create
public interface ITrafficLightsDefn extends IXingYiEntityDefn {
    @Field(readOnly = true)
    String id();
    String color();

    @Post(url = "/orange", state = "red")
    ITrafficLightsDefn orange();

    @Post(url = "/red", state = "flashing")
    ITrafficLightsDefn red();

    @Post(url = "/green", state = "yellow")
    ITrafficLightsDefn green();

    @Post(url = "/flashing", state = "green")
    ITrafficLightsDefn flashing();

}
