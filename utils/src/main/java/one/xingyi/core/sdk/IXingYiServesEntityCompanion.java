package one.xingyi.core.sdk;
import one.xingyi.core.endpoints.HasBookmarkAndUrl;
import one.xingyi.core.marshelling.MakesFromJson;
public interface IXingYiServesEntityCompanion<Defn extends IXingYiEntityDefn, T extends IXingYiEntity>   extends IXingYiServerCompanion<Defn,T>, HasBookmarkAndUrl, MakesFromJson<T> {
    String javascript();

}
