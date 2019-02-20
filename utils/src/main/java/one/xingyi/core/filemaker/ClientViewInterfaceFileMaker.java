package one.xingyi.core.filemaker;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.annotationProcessors.ActionsDom;
import one.xingyi.core.annotationProcessors.PostDom;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.codeDom.*;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.monad.MonadDefn;
import one.xingyi.core.sdk.IXingYiView;
import one.xingyi.core.utils.*;
import one.xingyi.core.validation.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
@RequiredArgsConstructor
public class ClientViewInterfaceFileMaker implements IFileMaker<ViewDomAndItsResourceDom> {

    final MonadDefn monadDefn;

    List<String> allFieldsAccessors(String interfaceName, List<ViewDomAndResourceDomField> dom) { return Lists.flatMap(dom, fd -> accessors(interfaceName, fd)); }

    List<String> accessors(String interfaceName, ViewDomAndResourceDomField viewDomAndEntityDom) {
        FieldDom dom = viewDomAndEntityDom.viewDomField;
        List<String> result = new ArrayList<>();
        result.add("//" + dom.typeDom);
        result.add(dom.typeDom.forView() + " " + dom.name + "();");
        if (!dom.readOnly && !viewDomAndEntityDom.entityDomField.map(e -> e.readOnly).orElse(false)) {
            result.add(interfaceName + " with" + dom.name + "(" + dom.typeDom.forView() + " " + dom.name + ");");
        }
        return result;
    }

    List<String> getMethod(String viewName, String companionName) {
        return List.of(
                "public static <T> " + monadDefn.simpleClassName() + "<T> get(HttpService" + monadDefn.simpleClassName() + " service, String id, Function<" + viewName + ", T> fn){return service.get(" + companionName + ",id,fn);}",
                "public static <T> " + monadDefn.simpleClassName() + "<Optional<T>> getOptional(HttpService" + monadDefn.simpleClassName() + " service, String id, Function<" + viewName + ", T> fn){return service.getOptional(" + companionName + ",id,fn);}");
    }
    List<String> editMethod(String viewName, String companionName) {
        return List.of("public static " + monadDefn.simpleClassName() + "<" + viewName +
                " > edit(HttpService" + monadDefn.simpleClassName() + " service, String id, Function<" + viewName + "," + viewName +
                "> fn){return service.edit(" + companionName + ",id, fn);}");
    }
    List<String> deleteMethod(String companionName) {
        return List.of("public static " + monadDefn.simpleClassName() + "<Boolean> delete(HttpService" + monadDefn.simpleClassName() + " service, String id){return service.delete(" + companionName + ",id);}");
    }
    List<String> createMethod(String viewName, String companionName) {
        return List.of("public static " + monadDefn.simpleClassName() + "<" + viewName + "> create(" + "HttpService" + monadDefn.simpleClassName() + " service, String id){return service.create(" + companionName + ",id);}");
    }
    List<String> createWithoutIdMethod(String viewName, String companionName) {
        return List.of("public static " + monadDefn.simpleClassName() + "<IdAndValue<" + viewName + ">> create(" + "HttpService" + monadDefn.simpleClassName() + " service, " + viewName + " view){return service.createWithoutId(" + companionName + ", view);}");
    }

    List<String> getRemoteAccessors(ViewDom viewDom, Optional<BookmarkUrlAndActionsDom> bookmarkUrlAndActionsDom) {
        String companionName = viewDom.viewNames.clientCompanion.asString() + ".companion";
        String viewName = viewDom.viewNames.clientView.asString();
        return Optionals.fold(bookmarkUrlAndActionsDom, () -> List.<String>of(), b -> {
            ActionsDom actionsDom = b.actionsDom;
            return Lists.<String>append(
                    Optionals.flatMap(actionsDom.getDom, dom -> getMethod(viewName, companionName)),
                    Optionals.flatMap(actionsDom.putDom, dom -> editMethod(viewName, companionName)),
                    Optionals.flatMap(actionsDom.createDom, dom -> createMethod(viewName, companionName)),
                    Optionals.flatMap(actionsDom.createWithoutIdDom, dom -> createWithoutIdMethod(viewName, companionName)),
                    Optionals.flatMap(actionsDom.deleteDom, dom -> deleteMethod(companionName)));
//                    Lists.flatMap(actionsDom.postDoms, postDom -> postMethod(postDom, viewName, companionName)));
        });
    }
    private List<String> postMethod(PostDom postDom, String viewName, String companionName) {
        return List.of("//The optional is because if the command needs a state, and that entity isn't in that state it will not be executed",
                "public static " + monadDefn.simpleClassName() + "<" + viewName + ">. " + postDom.action +
                        "(HttpService service, String id){return service.post(" + companionName + "," + Strings.quote(postDom.action) + ",id);}");
    }

    @Override public Result<String, FileDefn> apply(ViewDomAndItsResourceDom viewDomAndItsResourceDom) {
        if (viewDomAndItsResourceDom.entityDom.isEmpty()) return Result.failwith("could not create client view interface. Perhaps this is an incremental compilation issue and you need to do a full compile");
        ViewDom viewDom = viewDomAndItsResourceDom.viewDom;
        ResourceDom resourceDom = viewDomAndItsResourceDom.entityDom.get();

        Optional<BookmarkUrlAndActionsDom> accessDetails = BookmarkUrlAndActionsDom.create(viewDomAndItsResourceDom);
        List<String> manualImports = Lists.append(List.of(monadDefn.fullClassName(),
                "one.xingyi.core.httpClient.HttpService" + monadDefn.simpleClassName(),
                "one.xingyi.core.httpClient.client.view.UrlPattern"),
                Lists.unique(viewDom.fields.withDeprecatedmap(fd -> fd.typeDom.nested().fullTypeName())));
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(), viewDom.deprecated, viewDom.viewNames.originalDefn, "interface", viewDom.viewNames.clientView,
                        " extends IXingYiView<" + resourceDom.entityNames.clientResource.asString() + ">", manualImports,
                        IXingYiView.class, XingYiGenerated.class, Function.class, ServiceRequest.class, ServiceResponse.class,
                        IdAndValue.class, Optional.class),
                Formating.indent(getRemoteAccessors(viewDom, accessDetails)),
                List.of(),
                Formating.indent(allFieldsAccessors(viewDom.viewNames.clientView.className, viewDomAndItsResourceDom.viewDomAndResourceDomFields)),
                List.of("}")
        ), "\n");
        return Result.succeed(new FileDefn(viewDom.viewNames.clientView, result));
    }


}
