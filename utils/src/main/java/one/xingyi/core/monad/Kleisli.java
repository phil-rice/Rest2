package one.xingyi.core.monad;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface Kleisli<Req, Res, MRes> {
    <Res2, MRes2> Kleisli<Req, Res, MRes2> thenApply(Kleisli<Res, Res2, MRes2> k2);
}

@RequiredArgsConstructor
//OK Horrible, horrible type casting... but Java doesn't have higher kinded types, so we are sort of stuck with this abomination
class CompletableFutureKleisli<Req, Res> implements Kleisli<Req, Res, CompletableFuture<Res>> {
    final Function<Req, CompletableFuture<Res>> raw;
    @Override public <Res2, MRes2> Kleisli<Req, Res, MRes2> thenApply(Kleisli<Res, Res2, MRes2> k2) {
        Function<Res, CompletableFuture<Res2>> a = ((CompletableFutureKleisli<Res, Res2>) k2).raw;
        Function<Req, CompletableFuture<Res2>> result = req -> raw.apply(req).thenCompose(a);
        return (Kleisli<Req, Res, MRes2>) new CompletableFutureKleisli<Req, Res2>(result);
    }
}

