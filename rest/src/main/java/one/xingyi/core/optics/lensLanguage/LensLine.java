package one.xingyi.core.optics.lensLanguage;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.marshelling.JsonParser;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.utils.Lists;

import java.util.List;
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class LensLine {
    public final String lensName;
    public final List<LensDefn> defns;

    public String asString() {
        return lensName + "=" + Lists.mapJoin(defns, ",", d -> d.asString());
    }

}
