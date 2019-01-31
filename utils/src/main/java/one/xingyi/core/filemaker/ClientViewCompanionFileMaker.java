package one.xingyi.core.filemaker;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.client.IXingYi;
import one.xingyi.core.codeDom.ViewDom;
import one.xingyi.core.codeDom.ViewDomAndItsResourceDom;
import one.xingyi.core.endpoints.BookmarkAndUrlPattern;
import one.xingyi.core.monad.MonadDefn;
import one.xingyi.core.sdk.IXingYiClientViewCompanion;
import one.xingyi.core.sdk.IXingYiRemoteClientViewCompanion;
import one.xingyi.core.sdk.IXingYiView;
import one.xingyi.core.utils.*;
import one.xingyi.core.validation.Result;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
public class ClientViewCompanionFileMaker implements IFileMaker<ViewDomAndItsResourceDom> {
    final MonadDefn monadDefn;

    List<String> getMethod(String returnType) {
        return List.of("public <T> " + monadDefn.simpleClassName() + "<T> get(HttpService httpService, String id, Function<" + returnType + ", T> fn){ return httpService.get(this, id, fn);}");
    }
    List<String> createMethod(String returnType) {
        return List.of("public " + monadDefn.simpleClassName() + "<" + returnType + "> create(HttpService httpService, String id){ return httpService.create(this,id);}");
    }
    List<String> createWithoutIdMethod(String returnType) {
        return List.of("public " + monadDefn.simpleClassName() + "<IdAndValue<" + returnType + ">> createWithoutId(HttpService httpService){ return httpService.createWithoutId(this);}");
    }

    List<String> createDeleteMethod() {
        return List.of("public " + monadDefn.simpleClassName() + " <Boolean> delete(HttpService httpService, String id){ return httpService.delete(this,id);}");
    }
    List<String> createEditMethod(String returnType) {
        return List.of("public " + monadDefn.simpleClassName() + "<" + returnType + "> edit(HttpService httpService, String id, Function<" + returnType + "," + returnType + "> fn){ return httpService.edit(this, id, fn);}");
    }

    List<String> accessorMethods(ViewDom viewDom, BookmarkUrlAndActionsDom bookmarkUrlAndActionsDom) {
        return List.of();
//        ActionsDom actionsDom = bookmarkUrlAndActionsDom.actionsDom;
//        String viewReturnType = viewDom.viewNames.clientView.asString();
//        return Lists.<String>append(
//                Optionals.flatMap(actionsDom.getDom, dom -> getMethod(viewReturnType)),
//                Optionals.flatMap(actionsDom.createDom, dom -> createMethod(viewReturnType)),
//                Optionals.flatMap(actionsDom.createWithoutIdDom, dom -> createWithoutIdMethod(viewReturnType)),
//                Optionals.flatMap(actionsDom.deleteDom, dom -> createDeleteMethod()),
//                Optionals.flatMap(actionsDom.putDom, dom -> createEditMethod(viewReturnType))
//        );
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
                        List.of("@Override public String bookmark(){return " + Strings.quote(b.bookmarkAndUrlPattern.bookmark) + ";}",
                                "@Override public String acceptHeader(){return " + Strings.quote("not implemented yet") + ";}")));

    }

    @Override public Result<String, FileDefn> apply(ViewDomAndItsResourceDom viewDomAndItsResourceDom) {
        ViewDom viewDom = viewDomAndItsResourceDom.viewDom;
        Optional<BookmarkUrlAndActionsDom> accessDetails = BookmarkUrlAndActionsDom.create(viewDomAndItsResourceDom);
        String parentInterface = Optionals.fold(accessDetails, () -> "IXingYiClientViewCompanion", b -> "IXingYiRemoteClientViewCompanion");

        List<String> manualImports = Lists.append(
                List.of(monadDefn.fullClassName(),
                        "one.xingyi.core.httpClient.HttpService" + monadDefn.simpleClassName(),
                        "one.xingyi.core.httpClient.client.view.UrlPattern",
                        "one.xingyi.core.httpClient.client.companion.UrlPatternCompanion"),
                Lists.unique(viewDom.fields.withDeprecatedmap(fd -> fd.typeDom.nested().fullTypeName())));
//        String afterClassString = "<Entity extends IXingYiEntity, IOps extends IXingYiView<Entity>, Impl extends IXingYiClientImpl<Entity, IOps>> extends IXingYiClientMaker<Entity, IOps>"
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(), viewDom.deprecated, viewDom.viewNames.originalDefn, "class", viewDom.viewNames.clientCompanion,
                        " implements " + parentInterface + "<" +
                                viewDom.viewNames.clientEntity.asString() + "," +
                                viewDom.viewNames.clientView.asString() + "," +
                                viewDom.viewNames.clientViewImpl.asString() +
                                ">", manualImports,
                        IXingYiView.class, XingYiGenerated.class, IXingYiClientViewCompanion.class,
                        IXingYiRemoteClientViewCompanion.class, IXingYi.class, BookmarkAndUrlPattern.class,
                        Function.class, IdAndValue.class),

                List.of(Formating.indent + "static public " + viewDom.viewNames.clientCompanion.asString() + " companion = new " + viewDom.viewNames.clientCompanion.className + "();"),
                Formating.indent(createGetRemoteAccessMethods(accessDetails, viewDom)),
                List.of(Formating.indent + "@SuppressWarnings(\"unchecked\")@Override public " + viewDom.viewNames.clientView.asString() + " make(IXingYi xingYi, Object mirror){return new " + viewDom.viewNames.clientViewImpl.asString() + "(xingYi,mirror);} "),
                List.of("}")
        ), "\n");
        return Result.succeed(new FileDefn(viewDom.viewNames.clientCompanion, result));
    }
}
