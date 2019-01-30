package one.xingyi.reference3;
import one.xingyi.core.annotations.Server;
import one.xingyi.core.annotations.ValidateLens;
@Server
@ValidateLens("version1Lens")
@ValidateLens("version2Lens")
@ValidateLens("version3Lens")
public interface IPersonServerDefn {
}
