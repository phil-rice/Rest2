package org.invertthepyramid.involved.mdm;

import com.twitter.finagle.Service;
import org.invertthepyramid.involved.utilities.IErrorStrategy;
import org.invertthepyramid.involved.utilities.Getter;

import static org.invertthepyramid.involved.utilities.Wrap.map;
import static org.invertthepyramid.involved.utilities.Wrap.wrap;

public class MdmService<From, To> implements java.util.function.Function<From, To> {

    public final IErrorStrategy errorStrategy;
    public final Service<RequestChain, ResponseChain> mdmService;

    public final java.util.function.Function<From, RequestChain> makeRequestChain;

    public final java.util.function.Function<ResponseChain, To> fromResponseChain;

    public MdmService(IErrorStrategy errorStrategy, Service<RequestChain, ResponseChain> mdmService, java.util.function.Function<From, RequestChain> makeRequestChain, java.util.function.Function<ResponseChain, To> fromResponseChain) {
        this.errorStrategy = errorStrategy;
        this.mdmService = mdmService;
        this.makeRequestChain = makeRequestChain;
        this.fromResponseChain = fromResponseChain;
    }

    public Getter<To> resultGetter(From from) {return wrap(errorStrategy, map(mdmService.apply(makeRequestChain.apply(from)), fromResponseChain));}

    @Override
    public To apply(From from) {
        return resultGetter(from).get();
    }
}
