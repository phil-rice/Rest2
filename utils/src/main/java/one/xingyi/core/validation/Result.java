package one.xingyi.core.validation;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.utils.FunctionWithError;
import one.xingyi.core.utils.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
public interface Result<Fail, T> {
    List<Fail> fails();
    Optional<T> result();
    <T1> Result<Fail, T1> map(Function<T, T1> fn);
    <T1> Result<Fail, T1> flatMap(Function<T, Result<Fail, T1>> fn);
    <Fail1> Result<Fail1, T> failMap(FunctionWithError<Fail, Fail1> fn);

    static <Fail, T> Result<Fail, List<T>> merge(List<Result<Fail, T>> results) {
        return Result.<Fail, List<T>>apply(Lists.flatMap(results, f -> f.fails()), Lists.flatMapOptional(results, Result::result));
    }
    static <Fail, T> Result<Fail, T> validate(T t, Function<T, List<Fail>>... failFns) { return apply(Lists.flatMap(Arrays.asList(failFns), fn -> fn.apply(t)), t); }
    static <Fail, T> Result<Fail, T> succeed(T t) { return new Succeeds<>(t);}
    static <Fail, T> Result<Fail, T> fail(Fail fail) { return new Failures<>(List.of(fail));}
    static <Fail, T> Result<Fail, T> fails(Fail... fails) { return new Failures<>(Arrays.asList(fails));}
    static <Fail, T> Result<Fail, T> apply(List<Fail> fails, T t) { return fails.isEmpty() ? new Succeeds<>(t) : new Failures(fails);}
}

@ToString
@EqualsAndHashCode
class Failures<Fail, T> implements Result<Fail, T> {
    final List<Fail> fails;
    public Failures(List<Fail> fails) { this.fails = fails; }
    @Override public List<Fail> fails() { return fails; }
    @Override public Optional<T> result() { return Optional.empty(); }
    @Override public <T1> Result<Fail, T1> map(Function<T, T1> fn) { return new Failures<>(fails); }
    @Override public <T1> Result<Fail, T1> flatMap(Function<T, Result<Fail, T1>> fn) {return new Failures<>(fails); }
    @Override public <Fail1> Result<Fail1, T> failMap(FunctionWithError<Fail, Fail1> fn) { return new Failures<Fail1, T>(Lists.<Fail, Fail1>map(fails, fn)); }
}

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
class Succeeds<Fail, T> implements Result<Fail, T> {
    final T t;
    @Override public List<Fail> fails() { return List.of(); }
    @Override public Optional<T> result() { return Optional.of(t); }
    @Override public <T1> Result<Fail, T1> map(Function<T, T1> fn) { return new Succeeds<>(fn.apply(t)); }
    @Override public <T1> Result<Fail, T1> flatMap(Function<T, Result<Fail, T1>> fn) { return fn.apply(t); }
    @Override public <Fail1> Result<Fail1, T> failMap(FunctionWithError<Fail, Fail1> fn) { return new Succeeds<>(t); }
}