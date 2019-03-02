package one.xingyi.core.annotationProcessors;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.annotations.OptionalGet;

import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class ActionsDom {
    public final Optional<GetDom> getDom;
    public final Optional<OptionalGetDom> optionalGetDom;
    public final Optional<PutDom> putDom;
    public final Optional<DeleteDom> deleteDom;
    public final Optional<CreateDom> createDom;
    public final Optional<CreateWithoutIdDom> createWithoutIdDom;
    public final Optional<PrototypeDom> prototypeDom;
    public final Optional<PrototypeNoIdDom> prototypeNoIdDom;
    public final List<PostDom> postDoms;
}
