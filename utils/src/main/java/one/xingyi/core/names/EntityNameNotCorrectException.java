package one.xingyi.core.names;
public class EntityNameNotCorrectException extends RuntimeException {
    public EntityNameNotCorrectException(String entityName) {
        super("Entity name doesn't start with an I or it doesnt end in Defn. This should have been validated earlier: " + entityName);
    }
}
