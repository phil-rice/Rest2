package one.xingyi.core.filemaker;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.client.IXingYi;
import one.xingyi.core.codeDom.ResourceDom;
import one.xingyi.core.codeDom.ViewDom;
import one.xingyi.core.codeDom.ViewDomAndItsResourceDom;
import one.xingyi.core.endpoints.BookmarkCodeAndUrlPattern;
import one.xingyi.core.marshelling.FetchJavascript;
import one.xingyi.core.marshelling.JsonParserAndWriter;
import one.xingyi.core.mediatype.IMediaTypeClientDefn;
import one.xingyi.core.mediatype.JsonAndLensDefnClientMediaTypeDefn;
import one.xingyi.core.monad.MonadDefn;
import one.xingyi.core.names.EntityNames;
import one.xingyi.core.optics.lensLanguage.LensStoreParser;
import one.xingyi.core.sdk.IXingYiClientViewCompanion;
import one.xingyi.core.sdk.IXingYiRemoteClientViewCompanion;
import one.xingyi.core.sdk.IXingYiView;
import one.xingyi.core.utils.*;
import one.xingyi.core.validation.Result;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
public class ClientResourceCompanionFileMaker implements IFileMaker<ResourceDom> {
    final MonadDefn monadDefn;


    List<String> accessorMethods(ViewDom viewDom, BookmarkUrlAndActionsDom bookmarkUrlAndActionsDom) {
        return List.of();
    }

    List<String> primitiveMethod(ViewDom viewDom) {
        return List.of("public <T> " + monadDefn.simpleClassName() + "<T> primitive(HttpService" + monadDefn.simpleClassName() + " httpService, String method, String url, Function<" + viewDom.viewNames.clientView.asString() + ", T> fn){",
                Formating.indent + "return httpService.primitive(this.companion, method, url,fn);",
                "}");

    }
    List<String> getUrlPatternMethod(ViewDom viewDom) {
        return List.of("public " + monadDefn.simpleClassName() + "<String> getUrlPattern(HttpService" + monadDefn.simpleClassName() + " service) {return  UrlPatternCompanion.companion.primitive(service, \"get\",this.bookmark(), UrlPattern::urlPattern); }");

    }

    List<String> createGetRemoteAccessMethods(Optional<BookmarkUrlAndActionsDom> bookmarkAndAccessDom, ViewDom viewDom) {
        return Optionals.fold(bookmarkAndAccessDom, () -> List.of(), b ->
                Lists.append(accessorMethods(viewDom, b),
                        primitiveMethod(viewDom),
                        getUrlPatternMethod(viewDom),
                        List.of("@Override public String bookmark(){return " + Strings.quote(b.bookmarkCodeAndUrlPattern.bookmark) + ";}",
                                "@Override public String acceptHeader(){return " + Strings.quote("not implemented yet") + ";}")));

    }

    @Override public Result<String, FileDefn> apply(ResourceDom resourceDom) {
        Optional<BookmarkUrlAndActionsDom> accessDetails = BookmarkUrlAndActionsDom.create(resourceDom);
        String parentInterface = Optionals.fold(accessDetails, () -> "IXingYiClientViewCompanion", b -> "IXingYiRemoteClientViewCompanion");

        List<String> manualImports = Lists.append(
                List.of(monadDefn.fullClassName(),
                        "one.xingyi.core.httpClient.HttpService" + monadDefn.simpleClassName(),
                        "one.xingyi.core.httpClient.client.view.UrlPattern",
                        "one.xingyi.core.httpClient.client.viewcompanion.UrlPatternCompanion"),
                Lists.unique(resourceDom.fields.withDeprecatedmap(fd -> fd.typeDom.nested().fullTypeName())));
//        String afterClassString = "<Entity extends IXingYiEntity, IOps extends IXingYiView<Entity>, Impl extends IXingYiClientImpl<Entity, IOps>> extends IXingYiClientMaker<Entity, IOps>"
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(), resourceDom.deprecated, resourceDom.entityNames.originalDefn, "class", resourceDom.entityNames.clientResourceCompanion,
                        "", manualImports,
                        IXingYiView.class, XingYiGenerated.class, IXingYiClientViewCompanion.class,
                        IXingYiRemoteClientViewCompanion.class, IXingYi.class, BookmarkCodeAndUrlPattern.class, FetchJavascript.class, JsonParserAndWriter.class,
                        Function.class, IdAndValue.class, IMediaTypeClientDefn.class, JsonAndLensDefnClientMediaTypeDefn.class, LensStoreParser.class),

//                List.of(Formating.indent + "static public " + viewDom.viewNames.clientCompanion.asString() + " companion = new " + viewDom.viewNames.clientCompanion.className + "();"),
//                Formating.indent(createGetRemoteAccessMethods(accessDetails, viewDom)),
//                List.of(Formating.indent + "@SuppressWarnings(\"unchecked\")@Override public " + viewDom.viewNames.clientView.asString() + " make(IXingYi xingYi, Object mirror){return new " + viewDom.viewNames.clientViewImpl.asString() + "(xingYi,mirror);} "),
//                Formating.indent(createMediaType(viewDom)),
                List.of("}")
        ), "\n");
        return Result.succeed(new FileDefn(resourceDom.entityNames.clientResourceCompanion, result));
    }
    private List<String> createMediaType(ViewDom viewDom, ResourceDom resourceDom) {
        EntityNames entityNames = viewDom.viewNames.entityNames;

        return List.of("public <J>IMediaTypeClientDefn<" + resourceDom.entityNames.clientResource.asString() + "," + viewDom.viewNames.clientView.asString() + "> x(JsonParserAndWriter<J> json){",
                Formating.indent + "return new  JsonAndLensDefnClientMediaTypeDefn<>(" +
                        Strings.quote(entityNames.serverEntity.className) + ",json, FetchJavascript.asIs(),LensStoreParser.simple(),this);",
                "}");
    }
}
