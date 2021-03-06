package one.xingyi.core.utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
public class Optionals {
    //Optional<String> optBookmark = Optional.ofNullable(annotation).map(Entity::bookmarkAndUrl).flatMap(b -> serverNames.bookmarkAndUrl(entityNames, b));
//

    public static <T, T1> List<T1> toList(Optional<T> optT, Function<T, T1> fn) {
        List<T1> result = new ArrayList<>();
        optT.ifPresent(t -> result.add(fn.apply(t)));
        return result;
    }
    public static <T, T1> List<T1> flatMap(Optional<T> optT, Function<T, List<T1>> fn) {
        List<T1> result = new ArrayList<>();
        optT.ifPresent(t -> {
            for (T1 t1 : fn.apply(t))
                result.add(t1);
        });
        return result;
    }
    public static <T, T1> List<T1> flatMapif(Optional<T> optT, boolean b, Function<T, List<T1>> fn) {
        List<T1> result = new ArrayList<>();
        if (b) optT.ifPresent(t -> {
            for (T1 t1 : fn.apply(t))
                result.add(t1);
        });
        return result;
    }

    public static <T, T1> T1 fold(Optional<T> opt, Supplier<T1> notIn, Function<T, T1> in) {
        if (opt.isEmpty()) return notIn.get();
        else return in.apply(opt.get());
    }
    public static <T> void doit(Optional<T> opt, Runnable notIn, Consumer<T> in) {
        if (opt.isEmpty()) notIn.run();
        else in.accept(opt.get());
    }

    public static <T1, T2, T> Optional<T> join(Optional<T1> op1, Optional<T2> op2, BiFunction<T1, T2, T> fn) {
        return op1.flatMap(t1 -> op2.map(t2 -> fn.apply(t1, t2)));
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
