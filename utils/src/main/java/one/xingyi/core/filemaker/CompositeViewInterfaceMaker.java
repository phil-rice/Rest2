package one.xingyi.core.filemaker;
import one.xingyi.core.ISimpleMap;
import one.xingyi.core.annotations.XingYiGenerated;
import one.xingyi.core.client.IResourceList;
import one.xingyi.core.client.IXingYi;
import one.xingyi.core.codeDom.CompositeViewDom;
import one.xingyi.core.codeDom.FieldListDom;
import one.xingyi.core.codeDom.ResourceDom;
import one.xingyi.core.codeDom.ViewDom;
import one.xingyi.core.monad.CompletableFutureDefn;
import one.xingyi.core.monad.MonadDefn;
import one.xingyi.core.optics.Lens;
import one.xingyi.core.reflection.Reflection;
import one.xingyi.core.sdk.IXingYiClientImpl;
import one.xingyi.core.sdk.IXingYiCompositeView;
import one.xingyi.core.utils.Formating;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.PartialBiFunction;
import one.xingyi.core.validation.Result;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static one.xingyi.core.utils.PartialBiFunction.*;
public class CompositeViewInterfaceMaker implements IFileMaker<CompositeViewDom>, CreateViewMethods {


    List<String> fields(ResourceDom resourceDom, ViewDom viewDom) {
        return List.of("final IXingYi<" + resourceDom.entityNames.clientResource.asString() + "," + viewDom.viewNames.clientView.asString() + "> xingYi;", "final Object mirror;");
    }
    List<String> constructor(ResourceDom resourceDom, ViewDom viewDom) {
        String classname = viewDom.viewNames.clientViewImpl.className;
        FieldListDom fld = viewDom.fields;
        return List.of("public " + classname + "(IXingYi<" + resourceDom.entityNames.clientResource.asString() + "," + viewDom.viewNames.clientView.asString() + ">xingYi, Object mirror){", Formating.indent + "this.xingYi=xingYi;", Formating.indent + "this.mirror=mirror;", "}");
    }

    @Override public Result<String, FileDefn> apply(CompositeViewDom dom) {
        List<String> manualImports = List.of("one.xingyi.core.httpClient.HttpServiceCompletableFuture");
        try {
            Class.forName(dom.views.get(0).view.asString());

            String result = Lists.<String>join(Lists.<String>append(
                    Formating.javaFile(getClass(), false, dom.originalDefn, "interface", dom.clientInterface,
                            "extends " + IXingYiCompositeView.class.getName() + "<" + dom.clientResource.asString() + ">," + Lists.mapJoin(dom.views, ",", vc -> vc.view.asString()),
                            manualImports, IXingYi.class, IXingYiClientImpl.class, XingYiGenerated.class,
                            IResourceList.class, Lens.class, ISimpleMap.class, CompletableFuture.class, Function.class, Optional.class),
//                Formating.indent(getRemoteAccessors(dom.clientInterface.asString(), dom.companion, bookmarkUrlAndActionsDom)),
                    List.of("//" + dom.clientCompositeCompanion.asString()),
                    List.of("//" + dom.views.toString()),
                    Formating.indent(makeAccessors(dom)),
                    List.of("}")
            ), "\n");
            return Result.succeed(new FileDefn(dom.clientInterface, result));
        } catch (Exception e) {
            return Result.failwith(e.getClass() + " " + e.getMessage());
        }
    }

    private List<String> makeAccessors(CompositeViewDom dom) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(dom.views.get(0).view.asString());
        List<Method> methods = new Reflection<>(clazz).staticMethodsReturning(CompletableFuture.class);
//        throw new RuntimeException("methods are: " + methods+ "\n" + clazz);
        return Lists.flatMap(methods, m -> accessorFor(dom, m));

    }

    PartialBiFunction<CompositeViewDom, Method, List<String>> pfs = chain(
            pf((dom, method) -> method.getName().equalsIgnoreCase("get"), (d, m) -> List.of(
                    "public static <T> CompletableFuture<T> get(HttpServiceCompletableFuture service, String id, Function<" + d.clientInterface.className + ", T> fn){",
                    Formating.indent + "return service.get(" + d.clientCompositeCompanion.asString() + ".companion,id,fn);",
                    "}")),
            pf((dom, method) -> method.getName().equalsIgnoreCase("getOptional"), (d, m) -> List.of(
                    "public static <T> CompletableFuture<Optional<T>> getOptional(HttpServiceCompletableFuture service, String id, Function<" + d.clientInterface.className + ", T> fn){",
                    Formating.indent + "return service.getOptional(" + d.clientCompositeCompanion.asString() + ".companion,id,fn);",
                    "}")),
            pf((dom, method) -> method.getName().equalsIgnoreCase("edit"), (d, m) -> List.of("//edit")),
            pf((dom, method) -> method.getName().equalsIgnoreCase("create") && method.getParameterTypes().length == 2 && method.getParameterTypes()[1] == String.class, (d, m) -> List.of("//create1")),
            pf((dom, method) -> method.getName().equalsIgnoreCase("create"), (d, m) -> List.of("//create2")),
            pf((dom, method) -> method.getName().equalsIgnoreCase("delete"), (d, m) -> List.of("//delete"))
    );
    //  public static <T> CompletableFuture<T> get(HttpServiceCompletableFuture service, String id, Function<one.xingyi.reference1.person.client.view.PersonLine12View, T> fn){return service.get(one.xingyi.reference1.person.client.viewcompanion.PersonLine12ViewCompanion.companion,id,fn);}
    //    public static <T> CompletableFuture<Optional<T>> getOptional(HttpServiceCompletableFuture service, String id, Function<one.xingyi.reference1.person.client.view.PersonLine12View, T> fn){return service.getOptional(one.xingyi.reference1.person.client.viewcompanion.PersonLine12ViewCompanion.companion,id,fn);}
    //    public static CompletableFuture<one.xingyi.reference1.person.client.view.PersonLine12View > edit(HttpServiceCompletableFuture service, String id, Function<one.xingyi.reference1.person.client.view.PersonLine12View,one.xingyi.reference1.person.client.view.PersonLine12View> fn){return service.edit(one.xingyi.reference1.person.client.viewcompanion.PersonLine12ViewCompanion.companion,id, fn);}
    //    public static CompletableFuture<one.xingyi.reference1.person.client.view.PersonLine12View> create(HttpServiceCompletableFuture service, String id){return service.create(one.xingyi.reference1.person.client.viewcompanion.PersonLine12ViewCompanion.companion,id);}
    //    public static CompletableFuture<IdAndValue<one.xingyi.reference1.person.client.view.PersonLine12View>> create(HttpServiceCompletableFuture service, one.xingyi.reference1.person.client.view.PersonLine12View view){return service.createWithoutId(one.xingyi.reference1.person.client.viewcompanion.PersonLine12ViewCompanion.companion, view);}
    //    public static CompletableFuture<Boolean> delete(HttpServiceCompletableFuture service, String id){return service.delete(one.xingyi.reference1.person.client.viewcompanion.PersonLine12ViewCompanion.companion,id);}
    //

    private List<String> accessorFor(CompositeViewDom dom, Method method) {
        return pfs.orDefault(dom, method, () -> {
            throw new IllegalArgumentException("Could not work out how to process method " + method);
        });
    }
    @Override public MonadDefn monadDefn() {
        return new CompletableFutureDefn();
    }
}
