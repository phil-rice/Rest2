package one.xingyi.core.filemaker;
import one.xingyi.core.annotationProcessors.ActionsDom;
import one.xingyi.core.annotationProcessors.PostDom;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.codeDom.ViewDom;
import one.xingyi.core.codeDom.ViewDomAndEntityDomField;
import one.xingyi.core.codeDom.ViewDomAndItsEntityDom;
import one.xingyi.core.endpoints.BookmarkAndUrlPattern;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.sdk.IXingYiView;
import one.xingyi.core.utils.*;
import one.xingyi.core.validation.Result;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public class ClientViewInterfaceFileMaker implements IFileMaker<ViewDomAndItsEntityDom> {

    List<String> allFieldsAccessors(String interfaceName, List<ViewDomAndEntityDomField> dom) { return Lists.flatMap(dom, fd -> accessors(interfaceName, fd)); }

    List<String> accessors(String interfaceName, ViewDomAndEntityDomField viewDomAndEntityDom) {
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
                "public static <T> CompletableFuture<T> get(HttpService service, String id, Function<" + viewName + ", T> fn){return service.get(" + companionName + ",id,fn);}",
                "public static <T> CompletableFuture<Optional<T>> getOptional(HttpService service, String id, Function<" + viewName + ", T> fn){return service.getOptional(" + companionName + ",id,fn);}");
    }
    List<String> editMethod(String viewName, String companionName) {

        return List.of("public static CompletableFuture<" + viewName +
                " > edit(HttpService service, String id, Function<" + viewName + "," + viewName +
                "> fn){return service.edit(" + companionName + ",id, fn);}");
    }
    List<String> deleteMethod(String companionName) {
        return List.of("public static CompletableFuture<Boolean> delete(HttpService service, String id){return service.delete(" + companionName + ",id);}");
    }
    List<String> createMethod(String viewName, String companionName) {
        return List.of("public static CompletableFuture<" + viewName + "> create(HttpService service, String id){return service.create(" + companionName + ",id);}");
    }
    List<String> createWithoutIdMethod(String viewName, String companionName) {
        return List.of("public static CompletableFuture<IdAndValue<" + viewName + ">> create(HttpService service){return service.createWithoutId(" + companionName + ");}");
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
                "public static CompletableFuture<" + viewName + ">. " + postDom.action +
                        "(HttpService service, String id){return service.post(" + companionName + "," + Strings.quote(postDom.action) + ",id);}");
    }

    @Override public Result<String, FileDefn> apply(ViewDomAndItsEntityDom viewDomAndItsEntityDom) {
        ViewDom viewDom = viewDomAndItsEntityDom.viewDom;
        Optional<BookmarkUrlAndActionsDom> accessDetails = BookmarkUrlAndActionsDom.create(viewDomAndItsEntityDom);
        List<String> manualImports = Lists.append(List.of("one.xingyi.core.httpClient.HttpService", "one.xingyi.core.httpClient.client.view.UrlPattern"), Lists.unique(viewDom.fields.map(fd -> fd.typeDom.fullTypeName())));
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(), viewDom.viewNames.originalDefn, "interface", viewDom.viewNames.clientView,
                        " extends IXingYiView<" + viewDom.viewNames.clientEntity.asString() + ">", manualImports,
                        IXingYiView.class, XingYiGenerated.class, Function.class, ServiceRequest.class, ServiceResponse.class,
                        IdAndValue.class, CompletableFuture.class, Optional.class),
                Formating.indent(getRemoteAccessors(viewDom, accessDetails)),
                List.of(),
                Formating.indent(allFieldsAccessors(viewDom.viewNames.clientView.className, viewDomAndItsEntityDom.viewAndEntityFields)),
                List.of("}")
        ), "\n");
        return Result.succeed(new FileDefn(viewDom.viewNames.clientView, result));
    }


}
