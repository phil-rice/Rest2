package one.xingyi.core.annotations;
import java.lang.annotation.Repeatable;
@Repeatable(ValidateManyLens.class)
public @interface ValidateLens {
    /** The name of a file that holds a list of lens. Each lens name is on a separate line. Currently must be in root of source tree. Typically under /src/main/resources */
    String value();
    /** Should this generate an error at compile time or just a warning? */
    boolean error() default false;
    /** Causes an issue if the lens don't exactly match the specified file. Set to false, if you are validating that you provide support for legacy lens*/
    boolean exact() default true;
}
