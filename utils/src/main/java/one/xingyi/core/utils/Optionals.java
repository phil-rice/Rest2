package one.xingyi.core.utils;
import java.util.Optional;
import java.util.function.Supplier;
public class Optionals {

    public static <T> Optional<T> from(boolean b, Supplier<T> supplier) {
        if (b) return Optional.of(supplier.get());
        else return Optional.empty();
    }
    public static <T> Optional<T> from(boolean b, T t) {
        if (b) return Optional.of(t);
        else return Optional.empty();
    }
}
