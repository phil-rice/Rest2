package one.xingyi.core.filemaker;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.codeDom.PackageAndClassName;
@RequiredArgsConstructor
@EqualsAndHashCode

public class FileDefn {
    public final PackageAndClassName packageAndClassName;
    public final String content;
    @Override public String toString() { return "FileDefn(" + packageAndClassName + ")"; }
}
