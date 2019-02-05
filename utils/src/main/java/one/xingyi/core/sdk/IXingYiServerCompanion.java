package one.xingyi.core.sdk;

import one.xingyi.core.optics.lensLanguage.LensLine;

import java.util.List;
public interface IXingYiServerCompanion<Defn extends IXingYiResourceDefn, T extends IXingYiResource> {
    String javascript();
    List<String> lens();
    List<LensLine> lensLines();
}
