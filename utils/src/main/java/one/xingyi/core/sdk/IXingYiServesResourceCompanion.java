package one.xingyi.core.sdk;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.endpoints.EndpointContext;
import one.xingyi.core.endpoints.HasBookmarkAndUrl;
import one.xingyi.core.marshelling.MakesFromJson;
import one.xingyi.core.optics.lensLanguage.LensLine;
import one.xingyi.core.reflection.ReflectionOn;
import one.xingyi.core.utils.Lists;

import java.util.List;
public interface IXingYiServesResourceCompanion<Defn extends IXingYiResourceDefn, T extends IXingYiResource> extends IXingYiServerCompanion<Defn, T>, HasBookmarkAndUrl, MakesFromJson<T> {
    String javascript();
    List<LensLine> lensLines();
    default <J, Controller> List<EndPoint> allEndpoints(EndpointContext<J> context, Controller controller) {
        ReflectionOn<IXingYiServesResourceCompanion<Defn, T>> reflection = new ReflectionOn<>(this);
        return Lists.append(
                reflection.methodsParamsAndWithReturnType(EndPoint.class, x -> true, context, controller),
                reflection.methodsParamsAndWithReturnType(EndPoint.class, x -> true, context));
    }

}
