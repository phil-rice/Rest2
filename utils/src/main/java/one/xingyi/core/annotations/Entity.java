package one.xingyi.core.annotations;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME) // Need to keep them around for testing
public @interface Entity {

    /* Where the client goes for service discovery. */
    String bookmark() default "";
    /* The root url for the end points about this resource. The client does not have access to this except through the book marked url*/
    String rootUrl() default "";
}
