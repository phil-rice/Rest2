package one.xingyi.core.optics.lensLanguage;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Strings;

import java.util.function.Function;
public interface LensStoreParser extends Function<String, LensDefnStore> {
    static LensStoreParser simple() { return new SimpleLensStoreParser(LensLineParser.simple()); }
}
@RequiredArgsConstructor
class SimpleLensStoreParser implements LensStoreParser {
    final LensLineParser lineParser;
    @Override public LensDefnStore apply(String s) {
        return new LensDefnStore(Lists.map(Strings.split(s, "\n"), line -> lineParser.apply(line)));
    }
}
