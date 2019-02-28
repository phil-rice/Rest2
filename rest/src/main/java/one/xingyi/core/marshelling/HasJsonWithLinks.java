package one.xingyi.core.marshelling;
import java.util.function.Function;
public interface HasJsonWithLinks<Context,Entity> {

    <J> J toJsonWithLinks(JsonWriter<J> jsonWriter, ContextForJson context, Function<Entity, String> stateFn);
}
