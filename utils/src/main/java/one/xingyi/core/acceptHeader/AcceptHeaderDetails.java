package one.xingyi.core.acceptHeader;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class AcceptHeaderDetails {
    public final boolean valid;
   public final List<String> lensNames;

    public static AcceptHeaderDetails invalid() { return new AcceptHeaderDetails(false, List.of()); }
    public static AcceptHeaderDetails valid(List<String> lensNames) { return new AcceptHeaderDetails(true, lensNames); }
}
