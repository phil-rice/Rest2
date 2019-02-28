package one.xingyi.core.annotations;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/** marks a resource. The resource will have a 'create with prototype' method. */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})

public @interface Prototype {
}
