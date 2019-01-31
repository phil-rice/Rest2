package one.xingyi.core.monad;
public class EpicMonadDefn implements MonadDefn {
    @Override public String fullClassName() { return "one.xingyi.core.monad.Epic"; }
    @Override public String simpleClassName() {
        return "Epic";
    }
    @Override public String map() { return "thenApply"; }
    @Override public String flatMap() { return "thenCompose"; }
}
