package one.xingyi.core.annotationProcessors;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class PrototypeNoIdDom {
    public final String prototypeId;
    public final String url;
}
