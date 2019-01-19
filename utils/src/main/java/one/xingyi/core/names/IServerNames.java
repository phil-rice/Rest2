package one.xingyi.core.names;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.utils.Strings;

import java.util.Optional;
public interface IServerNames {
    static IServerNames simple(IPackageNameStrategy packageNameStrategy, IClassNameStrategy classNameStrategy) { return new SimpleServerNames(packageNameStrategy, classNameStrategy); }
    EntityNames entityName(String className, String annotationEntityname);
    ViewNames viewName(String className, String annotationViewName);
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
        return new EntityNames(originalDefn, serverEntity, serverCompanion, clientEntity, entityRoot);
    }
    @Override public ViewNames viewName(String className, String nameInViewAnnotation) {
        PackageAndClassName originalDefn = new PackageAndClassName(className);
        String originalPackage = originalDefn.packageName;
        String viewRoot = classNameStrategy.toRoot(originalDefn.className, nameInViewAnnotation);
        PackageAndClassName clientView = new PackageAndClassName(packageNameStrategy.toClientViews(originalPackage), classNameStrategy.toClientViews(viewRoot));
        PackageAndClassName clientCompanion = new PackageAndClassName(packageNameStrategy.toClientCompanion(originalPackage), classNameStrategy.toClientCompanion(viewRoot));
        return new ViewNames(originalDefn, clientView, clientCompanion);
    }
    @Override public String entityLensName(EntityNames entityElementName, String fieldName, String annotationLensName) {
        return Strings.from(annotationLensName, "lens_" + entityElementName.entityNameForLens + "_" + fieldName);
    }
    @Override public String entityLensPath(EntityNames entityElementName, String fieldName, String annotationLensPath) { return Strings.from(annotationLensPath, fieldName); }
    @Override public Optional<String> bookmark(EntityNames entityElementName, String annotationBookmark) {
        return Strings.from(annotationBookmark);
    }
    @Override public Optional<String> getUrl(EntityNames entityElementName, String annotationGetUrl) {
        return Strings.from(annotationGetUrl);
    }
}
