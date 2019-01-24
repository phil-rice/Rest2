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
    /* Where the server has the 'get' url. This is only available on the client: the server has to go to the bookmarkAndUrl to find it*/
    String getUrl() default "";
}
