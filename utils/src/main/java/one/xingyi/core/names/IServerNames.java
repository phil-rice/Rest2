package one.xingyi.core.names;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.endpoints.BookmarkCodeAndUrlPattern;
import one.xingyi.core.utils.Optionals;
import one.xingyi.core.utils.Strings;
import one.xingyi.core.validation.Result;

import java.util.Optional;
public interface IServerNames {
    static IServerNames simple(IPackageNameStrategy packageNameStrategy, IClassNameStrategy classNameStrategy) { return new SimpleServerNames(packageNameStrategy, classNameStrategy); }
    Result<String, EntityNames> entityName(String className);
    Result<String, ViewNames> viewName(String className, String entityClassName);
    String entityLensName(EntityNames entityElementName, String fieldName, String annotationLensName);
    String entityLensPath(EntityNames entityElementName, String fieldName, String annotationLensPath);
    Optional<BookmarkCodeAndUrlPattern> bookmarkAndUrl(EntityNames entityElementName, String annotationBookmark, String annotationGetUrl, String codeUrl);
}

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
class SimpleServerNames implements IServerNames {
    final IPackageNameStrategy packageNameStrategy;
    final IClassNameStrategy classNameStrategy;

    @Override public Result<String, EntityNames> entityName(String className) {
        PackageAndClassName originalDefn = new PackageAndClassName(className);
        String originalPackage = originalDefn.packageName;
        return classNameStrategy.toRoot("Entity", originalDefn.className).map(entityRoot -> {
            PackageAndClassName serverInterface = new PackageAndClassName(packageNameStrategy.toServerImplForDomain(originalPackage), classNameStrategy.toServerInterfaceForDomain(entityRoot));
            PackageAndClassName serverEntity = new PackageAndClassName(packageNameStrategy.toServerImplForDomain(originalPackage), classNameStrategy.toServerImplForDomain(entityRoot));
            PackageAndClassName serverCompanion = new PackageAndClassName(packageNameStrategy.toServerCompanion(originalPackage), classNameStrategy.toClientCompanion(entityRoot));
            PackageAndClassName clientEntity = new PackageAndClassName(packageNameStrategy.toClientEntityDefn(originalPackage), classNameStrategy.toClientEntityDefn(entityRoot));
            PackageAndClassName serverController = new PackageAndClassName(packageNameStrategy.toServerController(originalPackage), classNameStrategy.toServerController(entityRoot));
            return new EntityNames(originalDefn, serverInterface, serverEntity, serverCompanion, clientEntity, serverController,entityRoot);
        });
    }
    @Override public Result<String, ViewNames> viewName(String className, String interfaceName) {
        PackageAndClassName originalDefn = new PackageAndClassName(className);
        String originalPackage = originalDefn.packageName;
        return classNameStrategy.toRoot("View", originalDefn.className).flatMap(viewRoot -> {
            PackageAndClassName clientEntity = new PackageAndClassName(packageNameStrategy.toClientEntityDefn(originalPackage), classNameStrategy.toClientEntityDefn(viewRoot));
            PackageAndClassName clientViewInterface = new PackageAndClassName(packageNameStrategy.toClientViewInterface(originalPackage), classNameStrategy.toClientViewInterface(viewRoot));
            PackageAndClassName clientViewImpl = new PackageAndClassName(packageNameStrategy.toClientViewImpl(originalPackage), classNameStrategy.toClientViewImpl(viewRoot));
            PackageAndClassName clientCompanion = new PackageAndClassName(packageNameStrategy.toClientCompanion(originalPackage), classNameStrategy.toClientCompanion(viewRoot));
            return entityName(interfaceName).map(en ->
                    new ViewNames(originalDefn, clientEntity, clientViewInterface, clientViewImpl, clientCompanion, en));
        });
    }
    @Override public String entityLensName(EntityNames entityElementName, String fieldName, String annotationLensName) {
        return Strings.from(annotationLensName, "lens_" + entityElementName.entityNameForLens + "_" + fieldName);
    }
    @Override public String entityLensPath(EntityNames entityElementName, String fieldName, String annotationLensPath) { return Strings.from(annotationLensPath, fieldName); }
    @Override public Optional<BookmarkCodeAndUrlPattern> bookmarkAndUrl(EntityNames entityElementName, String annotationBookmark, String annotationGetUrl, String codeUrl) {//TODO review this business logic
        return Optionals.join(Strings.from(annotationBookmark), Strings.from(annotationGetUrl), (b,u)->new BookmarkCodeAndUrlPattern(b,u, Strings.from(codeUrl).orElse(b+"/code")));
    }

}
