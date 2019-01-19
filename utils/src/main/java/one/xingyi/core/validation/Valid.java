package one.xingyi.core.validation;
import java.util.List;
import java.util.function.Function;
public interface Valid<Fail, T> extends Function<T, List<Fail>> {
    static <Fail, T> Valid<Fail, T> check(Function<T, Boolean> isOk, Function<T, Fail> message) { return t -> isOk.apply(t) ? List.of() : List.of(message.apply(t)); }
}
