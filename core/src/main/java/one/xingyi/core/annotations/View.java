package one.xingyi.core.annotations;
/** This is a view on an entity. If the entity is marked with this it is both a view and an entity */
public @interface View {
    /** The name of the view.
     * <ul><li>If the name of the interface it marks is IEntity this defaults to EntityView. (it's </li>
     * <ul><li>If the name of the interface it marks is Ilowcase this defaults to EntityView. IlowcaseView </li>
     * <li>If the name of the interface it marks is Entity this defaults to EntityView.</li></ul>     */
    String viewName() default "";
    /** defaults some javascript that is added if the view is used. Each field can also define javascript*/
    String javascript() default "";

}
