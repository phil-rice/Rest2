package one.xingyi.core.sdk;
import one.xingyi.core.client.IXingYi;
public interface IXingYiView<T extends IXingYiClientEntity>{
    Object mirror();
    IXingYi xingYi();
}
