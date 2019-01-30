package one.xingyi.reference2;
import one.xingyi.core.annotations.Server;
import one.xingyi.core.annotations.ValidateLens;
@Server
@ValidateLens(value = "version1Lens", exact = false)
@ValidateLens("version2Lens")
public interface IPersonServerDefn {
}
