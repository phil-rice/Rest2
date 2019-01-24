package one.xingyi.trafficlights;
import one.xingyi.core.annotations.*;
import one.xingyi.core.sdk.IXingYiView;
import one.xingyi.core.sdk.IXingYiViewDefn;

@Get
@Delete
@Put
@CreateWithoutId
@Create("/{id}/new")
@View
public interface IColourViewDefn extends IXingYiViewDefn<ITrafficLightsDefn> {
    @Field(readOnly = true)
    String id();

    @Field(readOnly = true)
    String color();

    @Post(value = "/{id}/orange", state = "red")
    IColourViewDefn orange();
}
