package one.xingyi.core.filemaker;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.codeDom.*;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.monad.MonadDefn;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.sdk.IXingYiView;
import one.xingyi.core.typeDom.ListType;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.IdAndValue;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Strings;
import one.xingyi.core.validation.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;


@RequiredArgsConstructor
public class ClientViewInterfaceFileMaker implements IFileMaker<ViewDomAndItsResourceDom>, CreateViewMethods {

    final MonadDefn monadDefn;

    @Override public Result<String, FileDefn> apply(ViewDomAndItsResourceDom viewDomAndItsResourceDom) {
        if (viewDomAndItsResourceDom.entityDom.isEmpty()) return Result.failwith("could not create client view interface. Perhaps this is an incremental compilation issue and you need to do a full compile");
        ViewDom viewDom = viewDomAndItsResourceDom.viewDom;
        ResourceDom resourceDom = viewDomAndItsResourceDom.entityDom.get();
        String companionName = viewDom.viewNames.clientCompanion.asString() + ".companion";
        String viewName = viewDom.viewNames.clientView.asString();


        Optional<BookmarkUrlAndActionsDom> accessDetails = BookmarkUrlAndActionsDom.create(viewDomAndItsResourceDom);
        List<String> manualImports = Lists.append(List.of(monadDefn().fullClassName(),
                "one.xingyi.core.httpClient.HttpService" + monadDefn().simpleClassName(),
                "one.xingyi.core.httpClient.client.view.UrlPattern"),
                Lists.unique(viewDom.fields.withDeprecatedmap(fd -> fd.typeDom.nested().fullTypeName())));
        String result = Lists.join(Lists.append(
                Formating.javaFile(getClass(), viewDom.deprecated, viewDom.viewNames.originalDefn, "interface", viewDom.viewNames.clientView,
                        " extends IXingYiView<" + resourceDom.entityNames.clientResource.asString() + ">", manualImports,
                        IXingYiView.class, XingYiGenerated.class, Function.class, ServiceRequest.class, ServiceResponse.class,
                        IdAndValue.class, Optional.class, Lens.class),
                Formating.indent(getRemoteAccessors(viewName, companionName, accessDetails)),
                List.of(),
//                Formating.indent(allFieldsAccessors(viewDom.viewNames.clientView.className, viewDomAndItsResourceDom.viewDomAndResourceDomFields)),
                Formating.indent(allFieldAccessorsForView(viewDom.viewNames.clientCompanion, viewDom.viewNames.clientView.className, viewDomAndItsResourceDom.viewDomAndResourceDomFields)),
                List.of("}")
        ), "\n");
        return Result.succeed(new FileDefn(viewDom.viewNames.clientView, result));
    }

    List<String> viewAndEntityaccessors(PackageAndClassName companion, String interfaceName, ViewDomAndResourceDomField viewDomAndResourceDomField) {
        FieldDom viewDom = viewDomAndResourceDomField.viewDomField;
        Optional<FieldDom> entityDom = viewDomAndResourceDomField.entityDomField;
        List<String> result = new ArrayList<>();
        result.add("//View" + viewDomAndResourceDomField.viewDomField);
        result.add("//Entity" + viewDomAndResourceDomField.entityDomField);
        String lensName = entityDom.map(fd -> fd.lensName).orElse("not defined. Is this because of incremental compilation?");

        //TODO wow... really ugly
        if (viewDom.typeDom.primitive()) {
            result.add("default public Lens<" + interfaceName + "," + viewDom.typeDom.forView() + "> " + viewDom.name +
                    "Lens(){ return xingYi().stringLens(" + companion.asString() + ".companion, " + Strings.quote(lensName) + ");}");
        } else if (viewDom.typeDom instanceof ListType) {
            result.add("//" + viewDom.typeDom);
            result.add("default public Lens<" + interfaceName + "," + viewDom.typeDom.forView() + ">" +
                    viewDom.name + "Lens(){return xingYi().listLens(" + companion.asString() + ".companion, " + viewDom.typeDom.nested().viewCompanion() + ".companion," + Strings.quote(lensName) + ");}");

        } else {
            result.add("default public Lens<" + interfaceName + "," + viewDom.typeDom.forView() + ">" +
                    viewDom.name + "Lens(){return xingYi().objectLens(" + companion.asString() + ".companion, " + viewDom.typeDom.nested().viewCompanion() + ".companion," + Strings.quote(lensName) + ");}");

        }
        result.add("default public " + viewDom.typeDom.forView() + " " + viewDom.name + "(){ return " + viewDom.name + "Lens().get(this);};");
        if (!viewDom.readOnly && entityDom.map(f -> !f.readOnly).orElse(true)) {
            result.add("default public " + interfaceName + " with" + viewDom.name + "(" +
                    viewDom.typeDom.forView() + " " + viewDom.name + "){ return " + viewDom.name + "Lens().set(this," + viewDom.name + ");}");
        }
        return result;
    }

    List<String> allFieldAccessorsForView(PackageAndClassName companion, String interfaceName, List<ViewDomAndResourceDomField> fields) {
        return Lists.flatMap(fields, f -> viewAndEntityaccessors(companion, interfaceName, f));
    }

    @Override public MonadDefn monadDefn() {
        return monadDefn;
    }
}
