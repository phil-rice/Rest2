package one.xingyi.core.names;
public interface IClassNameStrategy extends INameStrategy {
    String toRoot(String classSimpleName, String nameInEntityAnnotation);

    IClassNameStrategy simple = new SimpleClassSimpleNameStrategy();
}

class SimpleClassSimpleNameStrategy implements IClassNameStrategy {
    @Override public String toRoot(String className, String nameInEntityAnnotation) {
        if (nameInEntityAnnotation != null && nameInEntityAnnotation.length() > 0) return nameInEntityAnnotation;
        if (className.startsWith("II")) return className.substring(1);
        if (className.length() > 1 && className.startsWith("I") && Character.isUpperCase(className.charAt(1))) return className.substring(1);
        return className + "Entity";
    }
    @Override public String toServerCompanion(String entityDefn) { return entityDefn + "Companion"; }
    @Override public String toClientViews(String entityDefn) { return entityDefn; }
    @Override public String toClientEntityDefn(String entityDefn) { return entityDefn + "Entity"; }
    @Override public String toClientCompanion(String entityDefn) { return entityDefn + "Companion"; }
    @Override public String toServerForDomain(String entityDefn) { return entityDefn; }
}
