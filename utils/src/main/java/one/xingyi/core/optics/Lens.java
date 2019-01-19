package one.xingyi.core.optics;

import java.util.function.Function;


public interface Lens<A, B> extends Setter<A, B>, Getter<A, B> {
    static <A, B> Lens<A, B> create(Getter<A, B> get, Setter<A, B> set) {return new LensImpl<>(get, set);}

    A transform(A a, Function<B, B> fn);
    <C> Lens<A, C> andThen(Lens<B, C> lens);
}
class LensImpl<A, B> implements Lens<A, B> {

    final Getter<A, B> getter;
    final Setter<A, B> setter;

    public LensImpl(Getter<A, B> getter, Setter<A, B> setter) {
        this.getter = getter;
        this.setter = setter;
    }
    public B get(A a) {
        return getter.get(a);
    }

    public A set(A a, B b) {
        return setter.set(a, b);
    }

    public <C> Lens<A, C> andThen(Lens<B, C> lens) {
        Getter<A, C> newGetter = getter.andThenGet(lens);
        Setter<A, C> newSetter = (a, c) -> setter.set(a, lens.set(get(a), c));
        return new LensImpl<A, C>(newGetter, newSetter);
    }

    public A transform(A a, Function<B, B> fn) {
        return setter.set(a, fn.apply(getter.get(a)));
    }


}
