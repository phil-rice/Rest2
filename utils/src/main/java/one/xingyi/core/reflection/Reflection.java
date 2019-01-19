package one.xingyi.core.reflection;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static one.xingyi.core.utils.WrappedException.wrapCallable;
@RequiredArgsConstructor
public class Reflection<T> {
    final Class<T> clazz;
    public Field field(String fieldName) { return wrapCallable(() -> clazz.getField(fieldName)); }
    public Method method0(String methodName, Class<?>... params) { return wrapCallable(() -> clazz.getMethod(methodName, params)); }
    public <A extends Annotation> A classAnnotation(Class<A> a) {
        A result = wrapCallable(() -> clazz.getAnnotation(a));
        if (result == null) throw new RuntimeException("could not find " + a + " for class " + clazz);
        return result;

    }
    public <A extends Annotation> A methodAnnotation(Class<A> a, String methodName, Class<?>... params) {
        A result = wrapCallable(() -> clazz.getMethod(methodName).getAnnotation(a));
        if (result == null)
            throw new RuntimeException("could not find " + a + " for method " + clazz + "." + methodName);
        return result;

    }
    public <A extends Annotation> A fieldAnnotation(Class<A> a, String fieldName) {
        A result = wrapCallable(() -> clazz.getField(fieldName).getAnnotation(a));
        if (result == null) throw new RuntimeException("could not find " + a + " for field " + clazz + "." + fieldName);
        return result;
    }

}