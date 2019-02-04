package one.xingyi.core.annotations;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME) // Need to keep them around for testing
public @interface Field {
    /** can only be read in this view. If the entity is readonly then the view is too */
    boolean readOnly() default false;
    /** The lens name associated with the view. Defaults to 'viewName_fieldName_typeName' */
    String lensName() default "";
    /** The actual lens into the object. Defaults to 'fieldName'. Syntax is
     *   xxx.yyy.  these are field names in the entity.
     *   special values of xxx include <firstItem> if we have a list */
    String lensPath() default "";
    /** overrides the defn associated with this view field (the default is to call the lens */
    String javascript() default "";
    boolean templated() default false;
}
