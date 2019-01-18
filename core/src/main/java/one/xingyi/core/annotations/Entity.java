package one.xingyi.core.annotations;
public @interface Entity {
    /** The name of the view.
     * <ul><li>If the name of the interface it marks is IEntity this defaults to Entity. (it's </li>
     * <ul><li>If the name of the interface it marks is Ilowcase this defaults to Entity. IlowcaseEntity </li>
     * <li>If the name of the interface it marks is Entity this defaults to EntityEntity.</li></ul>     */
    String entityName() default "";

    /* Where the client goes for service discovery. */
    String bookmark() default "";
    /* Where the server has the 'get' url. This is only available on the client: the server has to go to the bookmark to find it*/
    String getUrl() default "";
}
