package one.xingyi.core.codeDom;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.annotationProcessors.ElementFail;
import one.xingyi.core.annotations.Entity;
import one.xingyi.core.annotations.Server;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.validation.Result;

import javax.lang.model.element.Element;
@RequiredArgsConstructor
@ToString
@Entity
@EqualsAndHashCode
public class ServerDom {
    public final PackageAndClassName originalDefn;
    public final PackageAndClassName serverName;
    public final int port;
    public final CodeDom codeDom;

    //TODO Awful code.
    public static Result<ElementFail, ServerDom> create(IServerNames names, Element element, CodeDom codeDom) {
        Server server = element.getAnnotation(Server.class);
        String rawName = element.asType().toString();
        PackageAndClassName originalDefn = new PackageAndClassName(rawName);
        PackageAndClassName serverName = originalDefn.mapName(s -> s.substring(1, s.length() - 4));
        return Result.succeed(new ServerDom(originalDefn, serverName, server.port(), codeDom));
    }
}
