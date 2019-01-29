package one.xingyi.core.annotations;
import java.lang.annotation.Repeatable;
@Repeatable(ValidateManyLens.class)
public @interface ValidateLens {
    /** The name of a file that holds a list of lens. Each lens name is on a separate line. Currently must be in root of source tree. Typically under /src/main/resources */
    String value();
    /** Should this generate an error at compile time or just a warning? */
    boolean error() default false;
}
