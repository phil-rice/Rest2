package one.xingyi.core.utils;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
public class Optionals {
    //Optional<String> optBookmark = Optional.ofNullable(annotation).map(Entity::bookmark).flatMap(b -> serverNames.bookmark(entityNames, b));
//
    public static <T, T1, T2> Optional<T2> chainOpt(T nullable, Function<T, T1> fn1, Function<T1, Optional<T2>> fn2) { return Optional.ofNullable(nullable).map(fn1).flatMap(fn2); }
    public static <T, T1, T2> T2 chain(T nullable, Function<T, T1> fn1, T1 defaultValue, Function<T1, T2> fn2) { return fn2.apply(Optional.ofNullable(nullable).map(fn1).orElse(defaultValue)); }

    public static <T, T1> T1 fold(Optional<T> opt, Supplier<T1> notIn, Function<T, T1> in) {
        if (opt.isEmpty()) return notIn.get();
        else return in.apply(opt.get());
    }
    public static <T> void doit(Optional<T> opt, Runnable notIn, Consumer<T> in) {
        if (opt.isEmpty()) notIn.run();
        else in.accept(opt.get());
    }

    public static <T> CompletableFuture<Optional<T>> flip(Optional<CompletableFuture<T>> opt) {
        return opt.map(fut -> fut.thenApply(x -> Optional.of(x))).orElse(CompletableFuture.completedFuture(Optional.empty()));
    }


    public static <T> Optional<T> from(boolean b, Supplier<T> supplier) {
        if (b) return Optional.of(supplier.get());
        else return Optional.empty();
    }
    public static <T> Optional<T> from(boolean b, T t) {
        if (b) return Optional.of(t);
        else return Optional.empty();
    }
}
