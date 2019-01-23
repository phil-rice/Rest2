package one.xingyi.core.endpointDefn;
import java.util.List;
public interface LinkData< State> {
    String name();
    /** get,post,put,delete... */
    String method();
    String templatedPath();
    List<State> legalStates();
    boolean accept(State state);
}
