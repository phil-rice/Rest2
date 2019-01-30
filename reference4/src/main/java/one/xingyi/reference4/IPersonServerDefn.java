package one.xingyi.reference4;
import one.xingyi.core.annotations.Server;
import one.xingyi.core.annotations.ValidateLens;
@Server
@ValidateLens(value = "version1Lens", exact = false)
@ValidateLens(value = "version2Lens", exact = false)
@ValidateLens(value = "version3Lens", exact = false)
@ValidateLens("version4Lens")
public interface IPersonServerDefn {
}
