package one.xingyi.core.sdk;
import java.util.concurrent.CompletableFuture;
public interface IXingYiClientViewCompanion<Entity extends IXingYiClientEntity, IOps extends IXingYiView<Entity>, Impl extends IXingYiClientImpl<Entity, IOps>> extends IXingYiClientMaker<Entity, IOps> {

}
