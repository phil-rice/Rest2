package one.xingyi.core.monad;
public class EpicMonadDefn implements MonadDefn {
    @Override public String fullClassName() { return "one.xingyi.core.monad.Epic"; }
    @Override public String simpleClassName() {
        return "Epic";
    }
    @Override public String map() { return "map"; }
    @Override public String flatMap() { return "flatMap"; }
}
