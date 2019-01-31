package one.xingyi.core.codeDom;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.annotationProcessors.ElementFail;
import one.xingyi.core.annotations.Resource;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.validation.Result;

import javax.lang.model.element.Element;
@RequiredArgsConstructor
@ToString
@Resource
@EqualsAndHashCode
public class ServerDom {
    public final PackageAndClassName originalDefn;
    public final PackageAndClassName serverName;
    public final CodeDom codeDom;

    public static Result<ElementFail, ServerDom> create(IServerNames names, Element element, CodeDom codeDom) {
        String rawName = element.asType().toString();
        PackageAndClassName originalDefn = new PackageAndClassName(rawName);
        PackageAndClassName serverName = originalDefn.mapName(s -> s.substring(1, s.length() - 4));
        return Result.succeed(new ServerDom(originalDefn, serverName, codeDom));
    }
}
