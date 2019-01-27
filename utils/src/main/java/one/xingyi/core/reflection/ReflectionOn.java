package one.xingyi.core.reflection;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.WrappedException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
@RequiredArgsConstructor
public class ReflectionOn<T> {
    final T t;

    public <T> List<T> methodsWithReturnType(Class<T> returnClazz, Function<Method, Boolean> acceptor) {
        return WrappedException.wrapCallable(() -> {
            List<T> result = new ArrayList<>();
            for (Method m : t.getClass().getMethods())
                if (returnClazz.isAssignableFrom(m.getReturnType()))
                    if (acceptor.apply(m)) result.add((T) m.invoke(t));
            return result;
        });
    }

}
