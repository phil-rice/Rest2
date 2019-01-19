package one.xingyi.core.filemaker;
import one.xingyi.core.utils.LoggerAdapter;

import java.util.List;
public interface IFileMaker<T> {
    FileDefn apply(T t);
}
