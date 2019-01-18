package one.xingyi.core.optics;
public interface Getter<A, B> {
    B get(A a);
    default <C> Getter<A, C> andThenGet(Getter<B, C> other) {
        return a -> other.get(get(a));
    }

}
