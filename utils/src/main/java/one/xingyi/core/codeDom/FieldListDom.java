package one.xingyi.core.codeDom;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.utils.FunctionWithError;
import one.xingyi.core.utils.Lists;

import java.util.List;
import java.util.function.Function;
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class FieldListDom {
    public final List<FieldDom> allFields;

    public String mapJoin(String separator, Function<FieldDom, String> fn) {
        return Lists.mapJoin(allFields, separator, fn);
    }
    public <T> List<T> map(FunctionWithError<FieldDom, T> fn) {
        return Lists.map(allFields, fn);
    }
    public <T> List<T> flatMap(Function<FieldDom, List<T>> fn) {
        return Lists.flatMap(allFields, fn);
    }
}
