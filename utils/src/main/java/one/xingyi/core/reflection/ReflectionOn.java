package one.xingyi.core.reflection;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.WrappedException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
@RequiredArgsConstructor
public class ReflectionOn<T> {
    final T t;

    @SuppressWarnings("unchecked")
    public <Result> List<Method> findMethodsWithReturnType(Class<Result> returnClazz, Function<Method, Boolean> acceptor) {
        return WrappedException.wrapCallable(() -> {
            List<Method> result = new ArrayList<>();
            for (Method m : t.getClass().getMethods())
                if (returnClazz.isAssignableFrom(m.getReturnType()))
                    if (acceptor.apply(m)) result.add(m);
            return result;
        });
    }
    public <Result> List<Result> invokeNoParamMethodsWithReturnType(Class<Result> returnClazz, Function<Method, Boolean> acceptor) {
        return Lists.map(findMethodsWithReturnType(returnClazz, acceptor), m -> (Result) m.invoke(t));
    }
    public <Result> List<Result> methodsParamsAndWithReturnType(Class<Result> returnClazz, Function<Method, Boolean> acceptor, Object... params) {
        List<Method> filter = Lists.filter(findMethodsWithReturnType(returnClazz, acceptor), method -> {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == params.length) {
                for (int i = 0; i < parameterTypes.length; i++)
                    if (!parameterTypes[i].isAssignableFrom(params[i].getClass()))
                        return false;
                return true;
            }
            return false;
        });
        return Lists.map(filter, method -> (Result) method.invoke(t, params));
    }

}
