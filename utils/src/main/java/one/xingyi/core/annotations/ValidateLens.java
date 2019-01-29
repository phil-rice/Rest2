package one.xingyi.core.annotations;
public @interface ValidateLens {
    /** The name of a file that holds a list of lens. Each lens name is on a separate line. Currently must be in root of source tree. Typically under /src/main/resources */
    String value();
}
