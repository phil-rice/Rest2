package one.xingyi.core.filemaker;
import one.xingyi.core.annotationProcessors.ActionsDom;
import one.xingyi.core.annotationProcessors.PostDom;
import one.xingyi.core.codeDom.FieldDom;
import one.xingyi.core.codeDom.ViewDom;
import one.xingyi.core.codeDom.ViewDomAndResourceDomField;
import one.xingyi.core.monad.MonadDefn;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.Optionals;
import one.xingyi.core.utils.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public interface
CreateViewMethods {
    MonadDefn monadDefn();

    default List<String> accessors(String interfaceName, ViewDomAndResourceDomField viewDomAndEntityDom) {
        FieldDom dom = viewDomAndEntityDom.viewDomField;
        List<String> result = new ArrayList<>();
        result.add("//" + dom.typeDom);
        result.add(dom.typeDom.forView() + " " + dom.name + "();");
        if (!dom.readOnly && !viewDomAndEntityDom.entityDomField.map(e -> e.readOnly).orElse(false)) {
            result.add(interfaceName + " with" + dom.name + "(" + dom.typeDom.forView() + " " + dom.name + ");");
        }
        return result;
    }

    default List<String> getMethod(String viewName, String companionName) {
        return List.of(
                "public static <T> " + monadDefn().simpleClassName() + "<T> get(HttpService" + monadDefn().simpleClassName() + " service, String id, Function<" + viewName + ", T> fn){return service.get(" + companionName + ",id,fn);}",
                "public static <T> " + monadDefn().simpleClassName() + "<Optional<T>> getOptional(HttpService" + monadDefn().simpleClassName() + " service, String id, Function<" + viewName + ", T> fn){return service.getOptional(" + companionName + ",id,fn);}");
    }
    default List<String> editMethod(String viewName, String companionName) {
        return List.of("public static " + monadDefn().simpleClassName() + "<" + viewName +
                " > edit(HttpService" + monadDefn().simpleClassName() + " service, String id, Function<" + viewName + "," + viewName +
                "> fn){return service.edit(" + companionName + ",id, fn);}");
    }
    default List<String> prototypeNoIdMethod(String viewName, String companionName, String prototypeId) {
        return List.of("public static " + monadDefn().simpleClassName() + "<IdAndValue<" + viewName +
                ">> prototypeNoId(HttpService" + monadDefn().simpleClassName() + " service,  Function<" + viewName + "," + viewName +
                "> fn){return service.prototypeNoId(" + companionName + "," + Strings.quote(prototypeId) + ",fn);}");
    }
    default List<String> prototypeMethod(String viewName, String companionName, String prototypeId) {
        return List.of("public static " + monadDefn().simpleClassName() + "<" + viewName +
                " > prototype(HttpService" + monadDefn().simpleClassName() + " service, String id, Function<" + viewName + "," + viewName +
                "> fn){return service.prototype(" + companionName + "," + Strings.quote(prototypeId) + ",id,fn);}");
    }
    default List<String> deleteMethod(String companionName) {
        return List.of("public static " + monadDefn().simpleClassName() + "<Boolean> delete(HttpService" + monadDefn().simpleClassName() + " service, String id){return service.delete(" + companionName + ",id);}");
    }
    default List<String> createMethod(String viewName, String companionName) {
        return List.of("public static " + monadDefn().simpleClassName() + "<" + viewName + "> create(" + "HttpService" + monadDefn().simpleClassName() + " service, String id){return service.create(" + companionName + ",id);}");
    }
    default List<String> createWithoutIdMethod(String viewName, String companionName) {
        return List.of("public static " + monadDefn().simpleClassName() + "<IdAndValue<" + viewName + ">> create(" + "HttpService" + monadDefn().simpleClassName() + " service, " + viewName + " view){return service.createWithoutId(" + companionName + ", view);}");
    }

    default List<String> getRemoteAccessors(String viewName, String companionName, Optional<BookmarkUrlAndActionsDom> bookmarkUrlAndActionsDom) {
        return Optionals.fold(bookmarkUrlAndActionsDom, () -> List.<String>of(), b -> {
            ActionsDom actionsDom = b.actionsDom;
            return Lists.<String>append(
                    Optionals.flatMap(actionsDom.getDom, dom -> getMethod(viewName, companionName)),
                    Optionals.flatMap(actionsDom.putDom, dom -> editMethod(viewName, companionName)),
                    Optionals.flatMap(actionsDom.createDom, dom -> createMethod(viewName, companionName)),
                    Optionals.flatMap(actionsDom.createWithoutIdDom, dom -> createWithoutIdMethod(viewName, companionName)),
                    Optionals.flatMap(actionsDom.deleteDom, dom -> deleteMethod(companionName)),
                    Optionals.flatMap(actionsDom.prototypeDom, dom -> prototypeMethod(viewName, companionName, dom.prototypeId)),
                    Optionals.flatMap(actionsDom.prototypeNoIdDom, dom -> prototypeNoIdMethod(viewName, companionName, dom.prototypeId))
            );
//                    Lists.flatMap(actionsDom.postDoms, postDom -> postMethod(postDom, viewName, companionName)));
        });
    }
    default List<String> postMethod(PostDom postDom, String viewName, String companionName) {
        return List.of("//The optional is because if the command needs a state, and that entity isn't in that state it will not be executed",
                "public static " + monadDefn().simpleClassName() + "<" + viewName + ">. " + postDom.action +
                        "(HttpService service, String id){return service.post(" + companionName + "," + Strings.quote(postDom.action) + ",id);}");
    }

}
