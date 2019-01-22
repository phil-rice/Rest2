package one.xingyi.core.state;
import one.xingyi.core.sdk.IXingYiEntity;
import one.xingyi.core.sdk.IXingYiEntityDefn;
public interface IState<Defn extends IXingYiEntityDefn, Entity extends IXingYiEntity> {
    String state(Entity entity);
}
