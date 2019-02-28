package one.xingyi.core.filemaker;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.codeDom.PackageAndClassName;
import one.xingyi.core.monad.MonadDefn;
import one.xingyi.core.utils.Files;
import one.xingyi.core.validation.Result;
@RequiredArgsConstructor
public class HttpServiceFileMaker implements IFileMaker<MonadDefn> {
    final String packageName;
    @Override public Result<String, FileDefn> apply(MonadDefn monadDefn) {
        String content = Files.getText("HttpService.template").
                replace("<monad>", monadDefn.simpleClassName()).
                replace("<monadFullClassName>", monadDefn.fullClassName()).
                replace("<map>", monadDefn.map()).
                replace("<flatMap>", monadDefn.flatMap());
                ;
        return Result.succeed(new FileDefn(new PackageAndClassName(packageName, "HttpService"+monadDefn.simpleClassName()), content));
    }
}
