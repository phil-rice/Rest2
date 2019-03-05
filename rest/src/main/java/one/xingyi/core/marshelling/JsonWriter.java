package one.xingyi.core.marshelling;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.client.ISimpleList;
import one.xingyi.core.optics.Lens;

import java.util.LinkedHashMap;
import java.util.List;
public interface JsonWriter<J> {

    default <Context> String toJson(HasJson<Context> hasJson, Context context) {
        return fromJ(hasJson.toJson(this, context));
    }
    /**
     * Contract is that there are an even number and that the first is a string, the next is a J
     */
    J makeObject(Object... namesAndValues);
    <T extends Object> J makeList(IResourceList<T> items);
    default J makeList(List<Object> items) {return makeList(IResourceList.fromList(items));}
    <T extends Object> J makeSimpleList(ISimpleList<T> items);
    J liftString(String string);
    String fromJ(J j);


}


