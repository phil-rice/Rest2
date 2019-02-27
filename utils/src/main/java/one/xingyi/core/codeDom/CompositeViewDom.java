package one.xingyi.core.codeDom;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.annotationProcessors.ElementFail;
import one.xingyi.core.names.IClassNameStrategy;
import one.xingyi.core.names.IPackageNameStrategy;
import one.xingyi.core.sdk.IXingYiCompositeDefn;
import one.xingyi.core.sdk.IXingYiView;
import one.xingyi.core.utils.Lists;
import one.xingyi.core.utils.LoggerAdapter;
import one.xingyi.core.utils.Strings;
import one.xingyi.core.validation.Result;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class CompositeViewDom {
    public final List<ViewAndCompanion> views;
    public final PackageAndClassName originalDefn;
    public final PackageAndClassName clientResource;
    public final PackageAndClassName clientInterface;
    public final PackageAndClassName clientImpl;
    public final PackageAndClassName clientCompositeCompanion;

    static String compositeDefnName = IXingYiCompositeDefn.class.getName();

    static Result<String, ViewAndCompanion> findViewAndCompanion(Element element, TypeMirror interfaceMirror) {
        String typeAsName = interfaceMirror.toString();
        if (typeAsName.contains("<") || typeAsName.contains(">")) return Result.failwith("Cannot process interface " + typeAsName + " as it is higher kinded");
        try {
            Class<?> interfaceClass = Class.forName(typeAsName);
            if (!IXingYiView.class.isAssignableFrom(interfaceClass)) return Result.failwith("interface " + typeAsName + " doesn't extends IXingYiView");
            PackageAndClassName view = new PackageAndClassName(typeAsName);
            String rootPackage = Strings.allButLastSegment(".", view.packageName);
            PackageAndClassName companionName = new PackageAndClassName(view.packageName + "companion", view.className + "Companion");
            return Result.succeed(new ViewAndCompanion(view, companionName));
        } catch (Exception e) {
            return Result.failwith(e.getClass() + " " + e.getMessage());
        }
    }

    public static Result<ElementFail, CompositeViewDom> create(LoggerAdapter log, TypeElement element, IPackageNameStrategy packageNameStrategy, IClassNameStrategy classNameStrategy) {
        PackageAndClassName originaldefn = new PackageAndClassName(element.asType().toString());
        List<? extends TypeMirror> interfaces = new ArrayList<>(element.getInterfaces());
        String debug = Lists.mapJoin(interfaces, ",", i -> i.toString());
        Optional<? extends TypeMirror> optCompositeInterface = Lists.find(interfaces, i -> i.toString().startsWith(compositeDefnName));
        if (optCompositeInterface.isEmpty()) log.error(element, "Should extend " + compositeDefnName + "\n" + debug);
        TypeMirror compInt = optCompositeInterface.get();
        interfaces.remove(compInt);
        List<Result<String, ViewAndCompanion>> failsAndView = Lists.map(interfaces, i -> findViewAndCompanion(element, i));
        List<String> failures = Result.failures(failsAndView);
        if (failures.size() > 0) return Result.failwith(new ElementFail(failures.toString(), element));
        List<ViewAndCompanion> views = Result.successes(failsAndView);

        PackageAndClassName clientResource = new PackageAndClassName(Strings.extractFromOptionalEnvelope(compositeDefnName + "<", ">", compInt.toString()));
//        IServerNames serverNames = IServerNames.simple(packageNameStrategy, classNameStrategy);

        //        log.error(element, "found: " + clientResource);
        return Result.succeed(new CompositeViewDom(
                views,
                originaldefn,
                clientResource,
                new PackageAndClassName(packageNameStrategy.toCompositeInterface(originaldefn.packageName), classNameStrategy.toCompositeInterface(originaldefn.className)),
                new PackageAndClassName(packageNameStrategy.toCompositeImpl(originaldefn.packageName), classNameStrategy.toCompositeImpl(originaldefn.className)),
                new PackageAndClassName(packageNameStrategy.toCompositeCompanion(originaldefn.packageName), classNameStrategy.toCompositeCompanion(originaldefn.className))
        ));
    }
}
