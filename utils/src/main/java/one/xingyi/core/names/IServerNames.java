package one.xingyi.core.names;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.endpoints.BookmarkAndUrlPattern;
import one.xingyi.core.utils.Optionals;
import one.xingyi.core.utils.Strings;
import one.xingyi.core.validation.Result;

import java.util.Optional;
public interface IServerNames {
    static IServerNames simple(IPackageNameStrategy packageNameStrategy, IClassNameStrategy classNameStrategy) { return new SimpleServerNames(packageNameStrategy, classNameStrategy); }
    Result<String, EntityNames> entityName(String className, String annotationEntityname);
    Result<String, ViewNames> viewName(String className, String entityClassName, String annotationViewName);
    String entityLensName(EntityNames entityElementName, String fieldName, String annotationLensName);
    String entityLensPath(EntityNames entityElementName, String fieldName, String annotationLensPath);
    Optional<BookmarkAndUrlPattern> bookmarkAndUrl(EntityNames entityElementName, String annotationBookmark, String annotationGetUrl);
}

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
class SimpleServerNames implements IServerNames {
    final IPackageNameStrategy packageNameStrategy;
    final IClassNameStrategy classNameStrategy;

    @Override public Result<String, EntityNames> entityName(String className, String nameInEntityAnnotation) {
        PackageAndClassName originalDefn = new PackageAndClassName(className);
        String originalPackage = originalDefn.packageName;
        return classNameStrategy.toRoot("Entity", originalDefn.className, nameInEntityAnnotation).map(entityRoot -> {
            PackageAndClassName serverInterface = new PackageAndClassName(packageNameStrategy.toServerImplForDomain(originalPackage), classNameStrategy.toServerInterfaceForDomain(entityRoot));
            PackageAndClassName serverEntity = new PackageAndClassName(packageNameStrategy.toServerImplForDomain(originalPackage), classNameStrategy.toServerImplForDomain(entityRoot));
            PackageAndClassName serverCompanion = new PackageAndClassName(packageNameStrategy.toServerCompanion(originalPackage), classNameStrategy.toClientCompanion(entityRoot));
            PackageAndClassName clientEntity = new PackageAndClassName(packageNameStrategy.toClientEntityDefn(originalPackage), classNameStrategy.toClientEntityDefn(entityRoot));
            return new EntityNames(originalDefn, serverInterface, serverEntity, serverCompanion, clientEntity, entityRoot);
        });
    }
    @Override public Result<String, ViewNames> viewName(String className, String interfaceName, String nameInViewAnnotation) {
        PackageAndClassName originalDefn = new PackageAndClassName(className);
        String originalPackage = originalDefn.packageName;
        return classNameStrategy.toRoot("View", originalDefn.className, nameInViewAnnotation).flatMap(viewRoot -> {
            PackageAndClassName clientEntity = new PackageAndClassName(packageNameStrategy.toClientEntityDefn(originalPackage), classNameStrategy.toClientEntityDefn(viewRoot));
            PackageAndClassName clientViewInterface = new PackageAndClassName(packageNameStrategy.toClientViewInterface(originalPackage), classNameStrategy.toClientViewInterface(viewRoot));
            PackageAndClassName clientViewImpl = new PackageAndClassName(packageNameStrategy.toClientViewImpl(originalPackage), classNameStrategy.toClientViewImpl(viewRoot));
            PackageAndClassName clientCompanion = new PackageAndClassName(packageNameStrategy.toClientCompanion(originalPackage), classNameStrategy.toClientCompanion(viewRoot));
            return entityName(interfaceName, "").map(en ->
                    new ViewNames(originalDefn, clientEntity, clientViewInterface, clientViewImpl, clientCompanion, en));
        });
    }
    @Override public String entityLensName(EntityNames entityElementName, String fieldName, String annotationLensName) {
        return Strings.from(annotationLensName, "lens_" + entityElementName.entityNameForLens + "_" + fieldName);
    }
    @Override public String entityLensPath(EntityNames entityElementName, String fieldName, String annotationLensPath) { return Strings.from(annotationLensPath, fieldName); }
    @Override public Optional<BookmarkAndUrlPattern> bookmarkAndUrl(EntityNames entityElementName, String annotationBookmark, String annotationGetUrl) {//TODO review this business logic
        return Optionals.join(Strings.from(annotationBookmark), Strings.from(annotationGetUrl), BookmarkAndUrlPattern::new);
    }

}
