package one.xingyi.core.annotations;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/** This is a view on an entity. If the entity is marked with this it is both a view and an entity */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME) // Need to keep them around for testing
public @interface View {
      /** defaults some defn that is added if the view is used. Each field can also define defn*/
    String javascript() default "";

}
