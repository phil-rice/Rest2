package one.xingyi.core.endpoints;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class MethodAndPath {
    final String method;
    final String path;
}
