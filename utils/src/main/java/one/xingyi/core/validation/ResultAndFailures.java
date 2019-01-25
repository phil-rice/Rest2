package one.xingyi.core.validation;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
@RequiredArgsConstructor@ToString@EqualsAndHashCode
public class ResultAndFailures<Fail,T> {
    public final List<Fail> failures;
    public final T t;
}
