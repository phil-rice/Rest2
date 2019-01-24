package one.xingyi.core.annotationProcessors;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.names.ViewNames;
import one.xingyi.core.sdk.IXingYiViewDefn;
import one.xingyi.core.utils.Strings;
import one.xingyi.core.validation.Result;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.function.Function;
public interface IElementToViewNames extends Function<TypeElement, Result<ElementFail, ViewNames>> {
    static IElementToViewNames simple(IServerNames serverNames) {return new SimpleElementToViewNames(serverNames);}
}
class SimpleElementToViewNames implements IElementToViewNames {
    private IServerNames serverNames;
    public SimpleElementToViewNames(IServerNames serverNames) {this.serverNames = serverNames;}

    Result<String, String> findEntityName(TypeElement viewElement) {
        for (TypeMirror interfaceMirror : viewElement.getInterfaces()) {
            String interfaceName = interfaceMirror.toString();
            if (interfaceName.startsWith(IXingYiViewDefn.class.getName()))
                return Result.succeed(Strings.extractFromOptionalEnvelope(IXingYiViewDefn.class.getName() + "<", ">", interfaceName));
        }
        return Result.failwith("could not find interface for " + viewElement.asType() + " that was IXingYiViewDefn<> in " + viewElement.getInterfaces());
    }
    @Override public Result<ElementFail, ViewNames> apply(TypeElement element) {
        return ElementFail.lift(element, findEntityName(element).flatMap(en -> serverNames.viewName(element.asType().toString(), en)));
    }
}
