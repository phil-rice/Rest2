package one.xingyi.reference2;
import one.xingyi.core.annotations.Server;
import one.xingyi.core.annotations.ValidateLens;
@Server
@ValidateLens("version1Lens")
@ValidateLens("version2Lens")
public interface IPersonServerDefn {
}
