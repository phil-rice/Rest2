package one.xingyi.core.codeDom;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.annotations.Entity;
import one.xingyi.core.annotations.Server;

import javax.lang.model.element.Element;
@RequiredArgsConstructor
@ToString
@Entity
public class ServerDom {
    public final PackageAndClassName originalDefn;
    public final PackageAndClassName serverName;
    public final int port;
    public final CodeDom codeDom;

    public static ServerDom create(Element element, CodeDom codeDom) {
        Server server = element.getAnnotation(Server.class);
        String rawName = element.asType().toString();
        PackageAndClassName originalDefn = new PackageAndClassName(rawName);
        PackageAndClassName serverName = originalDefn.mapName(s -> s.substring(1, s.length() - 4));
        return new ServerDom(originalDefn, serverName, server.port(), codeDom);
    }
}
