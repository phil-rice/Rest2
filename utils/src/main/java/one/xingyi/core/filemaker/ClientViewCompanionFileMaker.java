package one.xingyi.core.filemaker;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.client.IXingYi;
import one.xingyi.core.codeDom.ViewDom;
import one.xingyi.core.codeDom.ViewDomAndItsEntityDom;
import one.xingyi.core.endpoints.BookmarkAndUrlPattern;
import one.xingyi.core.sdk.IXingYiClientViewCompanion;
import one.xingyi.core.sdk.IXingYiRemoteClientViewCompanion;
import one.xingyi.core.sdk.IXingYiView;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Optionals;
import one.xingyi.core.utils.Strings;
import one.xingyi.core.validation.Result;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public class ClientViewCompanionFileMaker implements IFileMaker<ViewDomAndItsEntityDom> {
    List<String> getMethod(ViewDom viewDom) {
        return List.of("public <T> CompletableFuture<T> get(HttpService httpService, String id, Function<" + viewDom.viewNames.clientView.asString() + ", T> fn){",
                Formating.indent + "return httpService.get(this,id,fn);",
                "}");
    }

    List<String> getPrimitiveMethod(ViewDom viewDom) {
        return List.of("public <T> CompletableFuture<T> getPrimitive(HttpService httpService, String url, Function<" + viewDom.viewNames.clientView.asString() + ", T> fn){",
                Formating.indent + "return httpService.primitiveGet(this.companion,url,fn);",
                "}");

    }
    List<String> getUrlPatternMethod(ViewDom viewDom) {
        return List.of("public CompletableFuture<String> getUrlPattern(HttpService service) {return  UrlPatternCompanion.companion.getPrimitive(service, this.bookmark(), UrlPattern::urlPattern); }");

    }

    List<String> createGetRemoteAccessMethods(Optional<BookmarkAndUrlPattern> bookmark, ViewDom viewDom) {
        return Optionals.fold(bookmark, () -> List.of(), b ->
                Lists.append(getMethod(viewDom),
                        getPrimitiveMethod(viewDom),
                        getUrlPatternMethod(viewDom),
                        List.of("@Override public String bookmark(){return " + Strings.quote(b.bookmark) + ";}",
                                "@Override public String acceptHeader(){return " + Strings.quote("not implemented yet") + ";}")));

    }


    @Override public Result<String, FileDefn> apply(ViewDomAndItsEntityDom viewDomAndItsEntityDom) {
        ViewDom viewDom = viewDomAndItsEntityDom.viewDom;
        Optional<BookmarkAndUrlPattern> accessDetails = viewDomAndItsEntityDom.entityDom.flatMap(ed -> ed.bookmark);
        String parentInterface = Optionals.fold(accessDetails, () -> "IXingYiClientViewCompanion", b -> "IXingYiRemoteClientViewCompanion");

        List<String> manualImports = Lists.append(
                List.of("one.xingyi.core.httpClient.HttpService", "one.xingyi.core.httpClient.client.view.UrlPattern", "one.xingyi.core.httpClient.client.companion.UrlPatternCompanion"),
                Lists.unique(viewDom.fields.map(fd -> fd.typeDom.fullTypeName())));
//        String afterClassString = "<Entity extends IXingYiEntity, IOps extends IXingYiView<Entity>, Impl extends IXingYiClientImpl<Entity, IOps>> extends IXingYiClientMaker<Entity, IOps>"
        Optional<BookmarkAndUrlPattern> bookmark = viewDomAndItsEntityDom.entityDom.flatMap(ed -> ed.bookmark);
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(), viewDom.viewNames.originalDefn, "class", viewDom.viewNames.clientCompanion,
                        " implements " + parentInterface + "<" +
                                viewDom.viewNames.clientEntity.asString() + "," +
                                viewDom.viewNames.clientView.asString() + "," +
                                viewDom.viewNames.clientViewImpl.asString() +
                                ">", manualImports,
                        IXingYiView.class, XingYiGenerated.class, IXingYiClientViewCompanion.class, IXingYiRemoteClientViewCompanion.class, IXingYi.class, BookmarkAndUrlPattern.class, CompletableFuture.class, Function.class),

                List.of(Formating.indent + "static public " + viewDom.viewNames.clientCompanion.asString() + " companion = new " + viewDom.viewNames.clientCompanion.className + "();"),
                Formating.indent(createGetRemoteAccessMethods(bookmark, viewDom)),
                List.of(Formating.indent + "@Override public " + viewDom.viewNames.clientView.asString() + " create(IXingYi xingYi, Object mirror){return new " + viewDom.viewNames.clientViewImpl.asString() + "(xingYi,mirror);} "),
                List.of("}")
        ), "\n");
        return Result.succeed(new FileDefn(viewDom.viewNames.clientCompanion, result));
    }
}
