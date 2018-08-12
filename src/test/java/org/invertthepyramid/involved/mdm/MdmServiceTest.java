package org.invertthepyramid.involved.mdm;

import com.twitter.finagle.Service;
import com.twitter.util.Await;
import com.twitter.util.Future;
import org.invertthepyramid.involved.mdm.MdmService;
import org.invertthepyramid.involved.mdm.RequestChain;
import org.invertthepyramid.involved.mdm.ResponseChain;
import org.invertthepyramid.involved.utilities.IErrorStrategy;
import org.invertthepyramid.involved.utilities.MapImpl;
import org.invertthepyramid.involved.utilities.WrapImpl;
import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MdmServiceTest {

    class Fixture {
        IErrorStrategy strategy = mock(IErrorStrategy.class);
        RequestChain requestChain = mock(RequestChain.class);
        ResponseChain responseChain = mock(ResponseChain.class);
        Function<String, RequestChain> makeRequestChain = mock(Function.class);
        Function<ResponseChain, Integer> makeResponseChain = mock(Function.class);
        Service<RequestChain, ResponseChain> mdmService = mock(Service.class);
        MdmService<String, Integer> service = new MdmService<>(strategy, mdmService, makeRequestChain, makeResponseChain);

        Fixture() {
            when(makeRequestChain.apply("from")).thenReturn(requestChain);
            when(mdmService.apply(requestChain)).thenReturn(Future.value(responseChain));
            when(makeResponseChain.apply(responseChain)).thenReturn(999);
        }

        void testResultGetterUsesWrapAndMap() throws Exception {
            WrapImpl<MapImpl<String, Integer>> wrap = (WrapImpl) service.resultGetter("from");
            assertEquals(strategy, wrap.strategy);
            MapImpl<ResponseChain, Integer> mapImpl = (MapImpl) wrap.getter;
            assertEquals(responseChain, Await.result(mapImpl.f));
            assertEquals(makeResponseChain, mapImpl.fn);
        }

    }

    @Test
    public void testGetter() throws Exception { new Fixture().testResultGetterUsesWrapAndMap(); }

    @Test
    public void testSmokeTest() { assertEquals(new Integer(999), new Fixture().service.apply("from")); }

}