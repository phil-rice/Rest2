package one.xingyi.core.monad;
public class CompletableFutureDefn implements MonadDefn {
    public String fullClassName() {return "java.util.concurrent.CompletableFuture";}
    public String simpleClassName() {return "CompletableFuture";}
    @Override public String map() { return "thenApply"; }
    @Override public String flatMap() { return "thenCompose"; }

}
