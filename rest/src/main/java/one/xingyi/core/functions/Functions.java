package one.xingyi.core.functions;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
public interface Functions {

    static <From, To> Function<From, To> constant(To to) {return r -> to;}
    static <From, To> Function<From, To> sideeffect(Consumer<From> sideeffect, Function<From, To> fn) { return from -> {sideeffect.accept(from); return fn.apply(from);}; }
    static <From, To> Function<From, CompletableFuture<To>> lift(Function<From, To> fn) { return fn.andThen(CompletableFuture::completedFuture); }
}
