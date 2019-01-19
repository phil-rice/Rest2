package one.xingyi.core.names;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.codeDom.PackageAndClassName;

import java.util.Optional;
public interface IServerNames {
    static IServerNames simple(IPackageNameStrategy packageNameStrategy, IClassNameStrategy classNameStrategy) { return new SimpleServerNames(packageNameStrategy, classNameStrategy); }
    EntityNames entityName(String className, String annotationEntityname);
    PackageAndClassName viewName(PackageAndClassName viewElementName, String viewEntityname);
    String entityLensName(EntityNames entityElementName, String fieldName, String annotationLensName);
    String entityLensPath(EntityNames entityElementName, String fieldName, String annotationLensPath);
    Optional<String> bookmark(EntityNames entityElementName, String annotationBookmark);
    Optional<String> getUrl(EntityNames entityElementName, String annotationGetUrl);
}

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
class SimpleServerNames implements IServerNames {
    final IPackageNameStrategy packageNameStrategy;
    final IClassNameStrategy classNameStrategy;

    @Override public EntityNames entityName(String className, String nameInEntityAnnotation) {
        PackageAndClassName originalDefn = new PackageAndClassName(className);
        String originalPackage = originalDefn.packageName;
        String entityRoot = classNameStrategy.toRoot(originalDefn.className, nameInEntityAnnotation);

        PackageAndClassName serverEntity = new PackageAndClassName(packageNameStrategy.toServerForDomain(originalPackage), classNameStrategy.toServerForDomain(entityRoot));
        PackageAndClassName serverCompanion = new PackageAndClassName(packageNameStrategy.toServerCompanion(originalPackage), classNameStrategy.toClientCompanion(entityRoot));
        PackageAndClassName clientEntity = new PackageAndClassName(packageNameStrategy.toClientEntityDefn(originalPackage), classNameStrategy.toClientEntityDefn(entityRoot));
        return new EntityNames(originalDefn, serverEntity, serverCompanion, clientEntity);
    }
    @Override public PackageAndClassName viewName(PackageAndClassName viewElementName, String viewEntityname) {
        return null;
    }
    @Override public String entityLensName(EntityNames entityElementName, String fieldName, String annotationLensName) {
        return null;
    }
    @Override public String entityLensPath(EntityNames entityElementName, String fieldName, String annotationLensPath) {
        return null;
    }
    @Override public Optional<String> bookmark(EntityNames entityElementName, String annotationBookmark) {
        return Optional.empty();
    }
    @Override public Optional<String> getUrl(EntityNames entityElementName, String annotationGetUrl) {
        return Optional.empty();
    }
}
