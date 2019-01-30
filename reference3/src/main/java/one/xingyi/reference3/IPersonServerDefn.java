package one.xingyi.reference3;
import one.xingyi.core.annotations.Server;
import one.xingyi.core.annotations.ValidateLens;
@Server
@ValidateLens(value = "version1Lens", exact = false)
@ValidateLens(value = "version2Lens", exact = false)
@ValidateLens("version3Lens")
public interface IPersonServerDefn {
}
