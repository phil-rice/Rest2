package one.xingyi.trafficlights;
import one.xingyi.core.annotations.*;
import one.xingyi.core.sdk.IXingYiResourceDefn;


@Resource(bookmark = "/lights", rootUrl = "{host}/lights/{id}")
@Delete
@Get(mustExist = false)
@Put
@CreateWithoutId(url = "{host}/lights")
@Create
public interface ITrafficLightsDefn extends IXingYiResourceDefn {
//    @Field(readOnly = true)
    String id();
    String color();
    String location();

    @Post(url = "/orange", state = "red")
    ITrafficLightsDefn orange(String id);

    @Post(url = "/red", state = "flashing")
    ITrafficLightsDefn red(String id);

    @Post(url = "/green", state = "yellow")
    ITrafficLightsDefn green(String id);

    @Post(url = "/flashing", state = "green")
    ITrafficLightsDefn flashing(String id);

}
