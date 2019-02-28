package one.xingyi.core.marshelling;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class DataToBeSentToClient {
    public final String data;
    public final String defn;

    public String asString() {return defn + IXingYiResponseSplitter.marker + data;}
}
