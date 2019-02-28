package one.xingyi.core.optics.lensLanguage;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.utils.Strings;

import java.util.List;
import java.util.function.Function;
public interface LensLineParser extends Function<String, LensLine> {
    static LensLineParser simple() {
        return new SimpleLensLineParser(LensValueParser.simple());
    }
}
@RequiredArgsConstructor
class SimpleLensLineParser implements LensLineParser {
    final LensValueParser parser;

    @Override public LensLine apply(String s) {
        List<String> parts = Strings.split(s, "=");
        if (parts.size() != 2) throw new RuntimeException("Expected a X = Y but got" + s);
        return new LensLine(parts.get(0), parser.apply(parts.get(1)));
    }
}
