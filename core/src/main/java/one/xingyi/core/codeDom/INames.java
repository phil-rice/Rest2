package one.xingyi.core.codeDom;
import java.util.Optional;
public interface INames {
    PackageAndClassName entityName(PackageAndClassName entityElementName, String annotationEntityname);
    PackageAndClassName viewName(PackageAndClassName viewElementName, String viewEntityname);
    String lensName(PackageAndClassName entityElementName, String fieldName, String annotationLensName);
    String lensPath(PackageAndClassName entityElementName, String fieldName, String annotationLensPath);
    Optional<String> bookmark(PackageAndClassName entityElementName, String annotationBookmark);
    Optional<String>getUrl(PackageAndClassName entityElementName, String annotationGetUrl);

}
