package one.xingyi.core.optics.lensLanguage;

import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.Lens;
public interface LensDefn<T> {
    String name();
    String asString();
    <J> Lens<J,T> asLens(JsonParserAndWriter<J> json);
}
