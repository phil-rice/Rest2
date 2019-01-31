package one.xingyi.core.monad;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.function.Function;

@RequiredArgsConstructor@ToString
public class Epic<T> {
    final T raw;
    public <T1> Epic<T1> map(Function<T, T1> fn) {return new Epic(fn.apply(raw));}
    public <T1> Epic<T1> flatMap(Function<T, Epic<T1>> fn) {return fn.apply(raw);}
}
