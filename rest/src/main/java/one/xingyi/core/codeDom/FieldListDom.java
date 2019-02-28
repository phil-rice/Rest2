package one.xingyi.core.codeDom;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.core.utils.FunctionWithException;
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
    public <T> List<T> noDeprecatedmap(FunctionWithException<FieldDom, T> fn) {
        return Lists.map(allNonDeprecatedFields, fn);
    }
    public <T> List<T> noDeprecatedflatMap(FunctionWithException<FieldDom, List<T>> fn) {
        return Lists.flatMap(allNonDeprecatedFields, fn);
    }

    public String withDeprecatedmapJoin(String separator, Function<FieldDom, String> fn) {
        return Lists.mapJoin(allFields, separator, fn);
    }
    public <T> List<T> withDeprecatedmap(FunctionWithException<FieldDom, T> fn) {
        return Lists.map(allFields, fn);
    }
    public <T> List<T> withDeprecatedflatMap(FunctionWithException<FieldDom, List<T>> fn) {
        return Lists.flatMap(allFields , fn);
    }
}
