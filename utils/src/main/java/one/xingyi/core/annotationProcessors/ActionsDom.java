package one.xingyi.core.annotationProcessors;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Optional;
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class ActionsDom {
    public final Optional<GetDom> getDom;
    public final Optional<PutDom> putDom;
    public final Optional<DeleteDom> deleteDom;
    public final Optional<CreateDom> createDom;
    public final Optional<CreateWithoutIdDom> createWithoutIdDom;
}
