package one.xingyi.core.annotations;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/* This is the primary id of an entity. Can only be set on the entity itself. */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME) // Need to keep them around for testing
public @interface PrimaryId {
}
