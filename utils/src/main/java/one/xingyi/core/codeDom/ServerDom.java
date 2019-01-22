package one.xingyi.core.codeDom;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.annotationProcessors.ElementFail;
import one.xingyi.core.annotations.Entity;
import one.xingyi.core.annotations.Server;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.sdk.IXingYiGet;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Strings;
import one.xingyi.core.validation.Result;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@ToString
@Entity
@EqualsAndHashCode
public class ServerDom {
    public final PackageAndClassName originalDefn;
    public final PackageAndClassName serverName;
    public final int port;
    public final List<EntityNameAndGet> defnNames;
    public final CodeDom codeDom;

    //TODO Awful code.
    public static Result<ElementFail, ServerDom> create(IServerNames names, Element element, List<TypeElement> getElements, CodeDom codeDom) {
        Server server = element.getAnnotation(Server.class);
        String rawName = element.asType().toString();
        PackageAndClassName originalDefn = new PackageAndClassName(rawName);
        PackageAndClassName serverName = originalDefn.mapName(s -> s.substring(1, s.length() - 4));
        return Result.merge(Lists.map(getElements, te -> {
            Optional<String> optFullInterfaceName = Lists.find(Lists.map(te.getInterfaces(), Object::toString), i -> i.startsWith(IXingYiGet.class.getName()));
            Result<ElementFail, String> resultFullInterfaceName = Result.from(optFullInterfaceName, () -> new ElementFail("Does not implement" + IXingYiGet.class.getName(), element));
            return resultFullInterfaceName.flatMap(s ->
                    names.entityName(Strings.extractFromOptionalEnvelope(IXingYiGet.class.getName() + "<", ">", s).split(",")[1], "").
                            map(en -> new EntityNameAndGet(te.asType().toString(),en)).
                            failMap(fail -> new ElementFail(fail, element)));
        })).map(defnNames -> new ServerDom(originalDefn, serverName, server.port(), defnNames, codeDom));
    }
}
