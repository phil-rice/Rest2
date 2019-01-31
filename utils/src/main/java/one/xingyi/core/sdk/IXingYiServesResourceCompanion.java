package one.xingyi.core.sdk;
import one.xingyi.core.endpoints.HasBookmarkAndUrl;
import one.xingyi.core.marshelling.MakesFromJson;
public interface IXingYiServesResourceCompanion<Defn extends IXingYiResourceDefn, T extends IXingYiResource>   extends IXingYiServerCompanion<Defn,T>, HasBookmarkAndUrl, MakesFromJson<T> {
    String javascript();

}
