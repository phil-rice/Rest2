package one.xingyi.core.codeDom;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.utils.FunctionWithError;
import one.xingyi.core.utils.Lists;

import java.util.List;
import java.util.function.Function;
@ToString
@EqualsAndHashCode
public class FieldListDom {
    public final List<FieldDom> allFields;
    public final List<FieldDom> allNonDeprecatedFields;
    public FieldListDom(List<FieldDom> allFields) {
        this.allFields = allFields;
        this.allNonDeprecatedFields = Lists.filter(allFields, fd -> !fd.deprecated);
    }

    public String noDeprecatedmapJoin(String separator, Function<FieldDom, String> fn) {
        return Lists.mapJoin(allNonDeprecatedFields, separator, fn);
    }
    public <T> List<T> noDeprecatedmap(FunctionWithError<FieldDom, T> fn) {
        return Lists.map(allNonDeprecatedFields, fn);
    }
    public <T> List<T> noDeprecatedflatMap(Function<FieldDom, List<T>> fn) {
        return Lists.flatMap(allNonDeprecatedFields, fn);
    }

    public String withDeprecatedmapJoin(String separator, Function<FieldDom, String> fn) {
        return Lists.mapJoin(allFields, separator, fn);
    }
    public <T> List<T> withDeprecatedmap(FunctionWithError<FieldDom, T> fn) {
        return Lists.map(allFields, fn);
    }
    public <T> List<T> withDeprecatedflatMap(Function<FieldDom, List<T>> fn) {
        return Lists.flatMap(allFields , fn);
    }
}
