package one.xingyi.core.sdk;

import java.util.List;
public interface IXingYiServerCompanion<Defn extends IXingYiResourceDefn, T extends IXingYiResource> {
    String javascript();
    List<String> lens();

}
