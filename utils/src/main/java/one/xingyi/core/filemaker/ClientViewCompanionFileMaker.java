package one.xingyi.core.filemaker;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.annotationProcessors.ActionsDom;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.client.IXingYi;
import one.xingyi.core.codeDom.ViewDom;
import one.xingyi.core.codeDom.ViewDomAndItsEntityDom;
import one.xingyi.core.endpoints.BookmarkAndUrlPattern;
import one.xingyi.core.sdk.IXingYiClientViewCompanion;
import one.xingyi.core.sdk.IXingYiRemoteClientViewCompanion;
import one.xingyi.core.sdk.IXingYiView;
import one.xingyi.core.utils.*;
import one.xingyi.core.validation.Result;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public class ClientViewCompanionFileMaker implements IFileMaker<ViewDomAndItsEntityDom> {
    List<String> getOrCreateMethod(String name, String returnType, String signature) {
        return List.of("public <T> CompletableFuture<T> " + name + "(HttpService httpService, String id, Function<" + returnType + ", T> fn){ return httpService." + signature + ";}");
    }
    List<String> createWithoutIdMethod(String returnType) {
        return List.of("public CompletableFuture<IdAndValue<" + returnType + ">> createWithoutId(HttpService httpService){ return httpService.createWithoutId(this);}");
    }

    List<String> createDeleteMethod() {
        return List.of("public CompletableFuture<Boolean> delete(HttpService httpService){ return httpService.delete(this);}");
    }
    List<String> createEditMethod(String returnType) {
        return List.of("public CompletableFuture<" + returnType + "> edit(HttpService httpService, String id, Function<" +returnType+ "," + returnType + "> fn){ return httpService.edit(this, id, fn);}");
    }

    List<String> accessorMethods(ViewDom viewDom, BookmarkUrlAndActionsDom bookmarkUrlAndActionsDom) {
        ActionsDom actionsDom = bookmarkUrlAndActionsDom.actionsDom;
        String viewReturnType = viewDom.viewNames.clientView.asString();
        return Lists.<String>append(
                Optionals.flatMap(actionsDom.getDom, dom -> getOrCreateMethod("get", viewReturnType, "get(this, id, fn)")),
                Optionals.flatMap(actionsDom.createDom, dom -> getOrCreateMethod("create", viewReturnType, "create(this, id, fn)")),
                Optionals.flatMap(actionsDom.createWithoutIdDom, dom -> createWithoutIdMethod(viewReturnType)),
                Optionals.flatMap(actionsDom.deleteDom, dom -> createDeleteMethod()),
                Optionals.flatMap(actionsDom.putDom, dom -> createEditMethod(viewReturnType))
        );
    }

    List<String> primitiveMethod(ViewDom viewDom) {
        return List.of("public <T> CompletableFuture<T> primitive(HttpService httpService, String method, String url, Function<" + viewDom.viewNames.clientView.asString() + ", T> fn){",
                Formating.indent + "return httpService.primitive(this.companion, method, url,fn);",
                "}");

    }
    List<String> getUrlPatternMethod(ViewDom viewDom) {
        return List.of("public CompletableFuture<String> getUrlPattern(HttpService service) {return  UrlPatternCompanion.companion.primitive(service, \"get\",this.bookmark(), UrlPattern::urlPattern); }");

    }

    List<String> createGetRemoteAccessMethods(Optional<BookmarkUrlAndActionsDom> bookmarkAndAccessDom, ViewDom viewDom) {
        return Optionals.fold(bookmarkAndAccessDom, () -> List.of(), b ->
                Lists.append(accessorMethods(viewDom, b),
                        primitiveMethod(viewDom),
                        getUrlPatternMethod(viewDom),
                        List.of("@Override public String bookmark(){return " + Strings.quote(b.bookmarkAndUrlPattern.bookmark) + ";}",
                                "@Override public String acceptHeader(){return " + Strings.quote("not implemented yet") + ";}")));

    }

    @RequiredArgsConstructor
    @ToString
    @EqualsAndHashCode
    class BookmarkUrlAndActionsDom {
        final BookmarkAndUrlPattern bookmarkAndUrlPattern;
        final ActionsDom actionsDom;
    }

    @Override public Result<String, FileDefn> apply(ViewDomAndItsEntityDom viewDomAndItsEntityDom) {
        ViewDom viewDom = viewDomAndItsEntityDom.viewDom;
        Optional<BookmarkUrlAndActionsDom> accessDetails = viewDomAndItsEntityDom.entityDom.flatMap(ed -> ed.bookmark.map(b -> new BookmarkUrlAndActionsDom(b, ed.actionsDom)));
        String parentInterface = Optionals.fold(accessDetails, () -> "IXingYiClientViewCompanion", b -> "IXingYiRemoteClientViewCompanion");

        List<String> manualImports = Lists.append(
                List.of("one.xingyi.core.httpClient.HttpService", "one.xingyi.core.httpClient.client.view.UrlPattern", "one.xingyi.core.httpClient.client.companion.UrlPatternCompanion"),
                Lists.unique(viewDom.fields.map(fd -> fd.typeDom.fullTypeName())));
//        String afterClassString = "<Entity extends IXingYiEntity, IOps extends IXingYiView<Entity>, Impl extends IXingYiClientImpl<Entity, IOps>> extends IXingYiClientMaker<Entity, IOps>"
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(), viewDom.viewNames.originalDefn, "class", viewDom.viewNames.clientCompanion,
                        " implements " + parentInterface + "<" +
                                viewDom.viewNames.clientEntity.asString() + "," +
                                viewDom.viewNames.clientView.asString() + "," +
                                viewDom.viewNames.clientViewImpl.asString() +
                                ">", manualImports,
                        IXingYiView.class, XingYiGenerated.class, IXingYiClientViewCompanion.class,
                        IXingYiRemoteClientViewCompanion.class, IXingYi.class, BookmarkAndUrlPattern.class,
                        CompletableFuture.class, Function.class, IdAndValue.class),

                List.of(Formating.indent + "static public " + viewDom.viewNames.clientCompanion.asString() + " companion = new " + viewDom.viewNames.clientCompanion.className + "();"),
                Formating.indent(createGetRemoteAccessMethods(accessDetails, viewDom)),
                List.of(Formating.indent + "@Override public " + viewDom.viewNames.clientView.asString() + " make(IXingYi xingYi, Object mirror){return new " + viewDom.viewNames.clientViewImpl.asString() + "(xingYi,mirror);} "),
                List.of("}")
        ), "\n");
        return Result.succeed(new FileDefn(viewDom.viewNames.clientCompanion, result));
    }
}
