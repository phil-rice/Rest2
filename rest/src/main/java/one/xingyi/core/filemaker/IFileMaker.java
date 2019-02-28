package one.xingyi.core.filemaker;
import one.xingyi.core.utils.LoggerAdapter;
import one.xingyi.core.validation.Result;

import java.util.List;
public interface IFileMaker<T> {
    Result<String, FileDefn> apply(T t);
}
