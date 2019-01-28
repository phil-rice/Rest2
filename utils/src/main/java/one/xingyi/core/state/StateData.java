package one.xingyi.core.state;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class StateData {
    public final String action;
    public final String link;
}
