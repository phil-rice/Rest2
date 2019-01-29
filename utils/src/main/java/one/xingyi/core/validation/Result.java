package one.xingyi.core.validation;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.utils.Function3;
import one.xingyi.core.utils.FunctionWithException;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Optionals;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
public interface Result<Fail, T> {
    List<Fail> fails();
    Optional<T> result();
    Result<Fail, T> forEach(Consumer<T> consumer);
    <T1> Result<Fail, T1> map(Function<T, T1> fn);
    <T1> Result<Fail, T1> flatMap(Function<T, Result<Fail, T1>> fn);
    <Fail1> Result<Fail1, T> failMap(FunctionWithException<Fail, Fail1> fn);

    static <Fail, T1, T2, T> Result<Fail, T> join(Result<Fail, T1> res1, Result<Fail, T2> res2, BiFunction<T1, T2, T> fn) {
        return res1.flatMap(r1 -> res2.map(r2 -> fn.apply(r1, r2)));
    }
    static <Fail, T1, T2, T3, T> Result<Fail, T> join(Result<Fail, T1> res1, Result<Fail, T2> res2, Result<Fail, T3> res3, Function3<T1, T2, T3, T> fn) {
        return res1.flatMap(r1 -> res2.flatMap(r2 -> res3.map(r3 -> fn.apply(r1, r2, r3))));
    }

    static <Fail, T> Result<Fail, T> from(Optional<T> optT, Supplier<Fail> supplier) { return Optionals.<T, Result<Fail, T>>fold(optT, () -> Result.failwith(supplier.get()), Result::succeed);}
    static <Fail, T> Result<Fail, List<T>> merge(List<Result<Fail, T>> results) {
        return Result.<Fail, List<T>>apply(Lists.flatMap(results, f -> f.fails()), Lists.flatMapOptional(results, Result::result));
    }
    @SafeVarargs static <Fail, T> Result<Fail, T> validate(T t, Function<T, List<Fail>>... failFns) { return apply(Lists.flatMap(Arrays.asList(failFns), fn -> fn.apply(t)), t); }
    static <Fail, T> Result<Fail, T> succeed(T t) { return new Succeeds<>(t);}
    static <Fail, T> Result<Fail, T> failwith(Fail fail) { return new Failures<>(List.of(fail));}
    @SafeVarargs static <Fail, T> Result<Fail, T> allFail(Fail... fails) { return new Failures<>(Arrays.asList(fails));}
    static <Fail, T> Result<Fail, T> apply(List<Fail> fails, T t) { return fails.isEmpty() ? new Succeeds<>(t) : new Failures<>(fails);}
    static <Fail, T> List<T> successes(List<Result<Fail, T>> results) { return Lists.flatMapOptional(results, Result::result);}
    static <Fail, T> List<Fail> failures(List<Result<Fail, T>> results) {return Lists.flatMap(results, Result::fails);}
}

@ToString
@EqualsAndHashCode
class Failures<Fail, T> implements Result<Fail, T> {
    final List<Fail> fails;
    public Failures(List<Fail> fails) { this.fails = fails; }
    @Override public List<Fail> fails() { return fails; }
    @Override public Optional<T> result() { return Optional.empty(); }
    @Override public Result<Fail, T> forEach(Consumer<T> consumer) { return this; }
    @Override public <T1> Result<Fail, T1> map(Function<T, T1> fn) { return new Failures<>(fails); }
    @Override public <T1> Result<Fail, T1> flatMap(Function<T, Result<Fail, T1>> fn) {return new Failures<>(fails); }
    @Override public <Fail1> Result<Fail1, T> failMap(FunctionWithException<Fail, Fail1> fn) { return new Failures<Fail1, T>(Lists.<Fail, Fail1>map(fails, fn)); }
}

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
class Succeeds<Fail, T> implements Result<Fail, T> {
    final T t;
    @Override public List<Fail> fails() { return List.of(); }
    @Override public Optional<T> result() { return Optional.of(t); }
    @Override public Result<Fail, T> forEach(Consumer<T> consumer) { consumer.accept(t); return this;}
    @Override public <T1> Result<Fail, T1> map(Function<T, T1> fn) { return new Succeeds<>(fn.apply(t)); }
    @Override public <T1> Result<Fail, T1> flatMap(Function<T, Result<Fail, T1>> fn) { return fn.apply(t); }
    @Override public <Fail1> Result<Fail1, T> failMap(FunctionWithException<Fail, Fail1> fn) { return new Succeeds<>(t); }
}