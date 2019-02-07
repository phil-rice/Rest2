package one.xingyi.core.marshelling;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.optics.Lens;
public interface JsonParserAndWriter<J> extends JsonParser<J>, JsonWriter<J> {
    Lens<J, J> lensToChild(String childname);
    Lens<J, String> lensToString(String name);
    Lens<J, Double> lensToDouble(String name);

    Lens<J, Integer> lensToInteger(String name);
    <T>Lens<J, IResourceList<T>> lensToResourceList(String name);
}
