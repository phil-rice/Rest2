package one.xingyi.reference1;
import one.xingyi.core.annotations.Server;
import one.xingyi.core.annotations.ValidateLens;
@Server(port = 9000)
@ValidateLens("version1Lens")
public interface IPersonServerDefn {
}
