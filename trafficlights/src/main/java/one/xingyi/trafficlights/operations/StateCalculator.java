package one.xingyi.trafficlights.operations;
import one.xingyi.core.sdk.IXingYiEntity;

import java.util.function.Function;
public interface StateCalculator<Entity extends IXingYiEntity, State> extends Function<Entity,State> {
}
