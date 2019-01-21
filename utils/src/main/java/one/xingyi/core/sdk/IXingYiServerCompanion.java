package one.xingyi.core.sdk;
import one.xingyi.core.endpoints.BookmarkAndUrlPattern;
import one.xingyi.core.endpoints.HasBookmarkAndUrl;
import one.xingyi.core.javascript.JavascriptStore;

import java.util.Optional;
public interface IXingYiServerCompanion<Defn extends IXingYiEntityDefn, T extends IXingYiEntity>   {
    String javascript();

}
