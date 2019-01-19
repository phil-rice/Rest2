package one.xingyi.core.sdk;
import one.xingyi.core.javascript.JavascriptStore;

import java.util.Optional;
public interface IXingYiServerCompanion<Defn extends IXingYiEntityDefn, T extends IXingYiEntity> {

    Optional<String> bookmark();
//    JavascriptStore javascriptStore();
}
