package one.xingyi.core.endpointDefn;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.core.http.ServiceRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class EndPointDefnData<State, Req, Res> implements LinkData<State> {
    final String name;
    final String method;
    final String templatedPath;
    final List<State> legalStates;
    /** This finds the request. If it is called the request matches the method and the templatedPath */
    final Function<ServiceRequest, Req> fromFn;
    /** Do what ever the end point does. */
    final Function<Req, CompletableFuture<Res>> fn;
    final Function<Res, Integer> statusCodeFn;
    @Override public String name() { return name; }
    @Override public String method() { return method; }
    @Override public String templatedPath() { return templatedPath; }
    @Override public List<State> legalStates() { return legalStates; }

    public boolean accept(State state) {
        return legalStates.isEmpty() || legalStates.contains(state);
    }
}

