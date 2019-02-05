package one.xingyi.core.sdk;
import one.xingyi.core.endpoints.HasBookmarkAndUrl;
import one.xingyi.core.marshelling.MakesFromJson;
import one.xingyi.core.optics.lensLanguage.LensLine;

import java.util.List;
public interface IXingYiServesResourceCompanion<Defn extends IXingYiResourceDefn, T extends IXingYiResource>   extends IXingYiServerCompanion<Defn,T>, HasBookmarkAndUrl, MakesFromJson<T> {
    String javascript();
    List<LensLine> lensLines();

}
