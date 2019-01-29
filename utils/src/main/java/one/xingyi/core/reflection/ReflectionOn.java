package one.xingyi.core.reflection;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.utils.WrappedException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
@RequiredArgsConstructor
public class ReflectionOn<T> {
    final T t;

    @SuppressWarnings("unchecked")
    public <Result> List<Result> methodsWithReturnType(Class<Result> returnClazz, Function<Method, Boolean> acceptor) {
        return WrappedException.wrapCallable(() -> {
            List<Result> result = new ArrayList<>();
            for (Method m : t.getClass().getMethods())
                if (returnClazz.isAssignableFrom(m.getReturnType()))
                    if (acceptor.apply(m)) result.add((Result) m.invoke(t));
            return result;
        });
    }

}
