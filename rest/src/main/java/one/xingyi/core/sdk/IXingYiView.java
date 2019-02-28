package one.xingyi.core.sdk;
import one.xingyi.core.client.IXingYi;
public interface IXingYiView<T extends IXingYiClientResource>{
    Object mirror();
    IXingYi xingYi();
}
