package one.xingyi.core.annotations;
public @interface Field {
    /** can only be read in this view. If the entity is readonly then the view is too */
    boolean readOnly() default false;
    /** The lens name associated with the view. Defaults to 'viewName_fieldName_typeName' */
    String lensName() default "";
    /** The actual lens into the object. Defaults to 'fieldName'. Syntax is
     *   xxx.yyy.  these are field names in the entity.
     *   special values of xxx include <firstItem> if we have a list */
    String lensPath() default "";
    /** overrides the javascript associated with this view field (the default is to call the lens */
    String javascript() default "";
}
