package one.xingyi.core.sdk;
import one.xingyi.core.endpoints.HasBookmarkAndUrl;
public interface IXingYiServesEntityCompanion<Defn extends IXingYiEntityDefn, T extends IXingYiEntity>   extends IXingYiServerCompanion<Defn,T>, HasBookmarkAndUrl {
    String javascript();

}
