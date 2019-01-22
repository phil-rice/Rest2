package one.xingyi.core.filemaker;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.codeDom.ViewDom;
import one.xingyi.core.codeDom.ViewDomAndEntityDomField;
import one.xingyi.core.codeDom.ViewDomAndItsEntityDom;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.sdk.IXingYiView;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;

import java.util.ArrayList;
import java.util.List;
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

    List<String> getMethod(ViewDom viewDom) {
        return List.of("public static <T> CompletableFuture<T> get(HttpService service, String id, Function<" + viewDom.viewNames.clientView.asString() + ", T> fn){",
                Formating.indent + "return " + viewDom.viewNames.clientCompanion.asString() + ".companion.get(service,id,fn);",
                "}");
    }

    List<String> getPrimitiveMethod(ViewDom viewDom) {
        return List.of("public static <T> CompletableFuture<T> getPrimitive(HttpService service, String url, Function<" + viewDom.viewNames.clientView.asString() + ", T> fn){",
                Formating.indent + "return " + viewDom.viewNames.clientCompanion.asString() + ".companion.getPrimitive(service,url,fn);",
                "}");

    }
    List<String> getUrlPatternMethod(ViewDom viewDom) {
        return List.of("public static CompletableFuture<String> getUrlPattern(HttpService service) {return  " + viewDom.viewNames.clientCompanion.asString() + ".companion.getUrlPattern(service); }");

    }

    @Override public FileDefn apply(ViewDomAndItsEntityDom viewDomAndItsEntityDom) {
        ViewDom viewDom = viewDomAndItsEntityDom.viewDom;
        List<String> manualImports = Lists.append(List.of("one.xingyi.core.httpClient.HttpService", "one.xingyi.core.httpClient.client.view.UrlPattern"), Lists.unique(viewDom.fields.map(fd -> fd.typeDom.fullTypeName())));
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(),viewDom.viewNames.originalDefn, "interface", viewDom.viewNames.clientView,
                        " extends IXingYiView<" + viewDom.viewNames.clientEntity.asString() + ">", manualImports,
                        IXingYiView.class, XingYiGenerated.class, Function.class, ServiceRequest.class, ServiceResponse.class, CompletableFuture.class),
                Formating.indent(getPrimitiveMethod(viewDom)),
                Formating.indent(getUrlPatternMethod(viewDom)),
                Formating.indent(getMethod(viewDom)),
                List.of(),
                Formating.indent(allFieldsAccessors(viewDom.viewNames.clientView.className, viewDomAndItsEntityDom.viewAndEntityFields)),
                List.of("}")
        ), "\n");
        return new FileDefn(viewDom.viewNames.clientView, result);
    }


}
