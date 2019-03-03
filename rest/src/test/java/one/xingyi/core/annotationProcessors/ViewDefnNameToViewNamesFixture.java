package one.xingyi.core.annotationProcessors;
import one.xingyi.core.names.IClassNameStrategy;
import one.xingyi.core.names.IPackageNameStrategy;
import one.xingyi.core.names.IServerNames;
import one.xingyi.core.names.ViewNames;

import java.util.Map;
public interface ViewDefnNameToViewNamesFixture {
    IServerNames serverNames = IServerNames.simple(IPackageNameStrategy.simple, IClassNameStrategy.simple);
    ViewNames one = serverNames.viewName("one.view.IOneViewDefn", "one.some.unimportant.for.this.Class");
    ViewNames two = serverNames.viewName("one.view.ITwoViewDefn", "one.some.unimportant.for.this.Class");
    ViewNames personViewNames = serverNames.viewName("a.b.IPersonViewDefn", "a.b.IPersonDefn");

    IViewDefnNameToViewName viewToViewNames = IViewDefnNameToViewName.simple(
            Map.of(one.originalDefn.className, one,
                    two.originalDefn.className, two,
                    personViewNames.originalDefn.className, personViewNames));

}
