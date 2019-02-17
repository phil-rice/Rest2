package one.xingyi.core;
import lombok.RequiredArgsConstructor;
import one.xingyi.core.endpoints.EndPoint;
import one.xingyi.core.endpoints.EndpointContext;
import one.xingyi.core.endpoints.HasBookmarkAndUrl;
import one.xingyi.core.endpoints.MethodAndPath;
import one.xingyi.core.http.ServiceRequest;
import one.xingyi.core.http.ServiceResponse;
import one.xingyi.core.httpClient.ResourceDetailsRequest;
import one.xingyi.core.httpClient.server.companion.ResourceDetailsCompanion;
import one.xingyi.core.httpClient.server.domain.ResourceDetails;
import one.xingyi.core.mediatype.IXingYiServerMediaTypeDefn;
import one.xingyi.core.utils.Lists;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class ResourceDefinitionEndPoint implements EndPoint {
    final EntityRegister register;
    final IXingYiServerMediaTypeDefn<ResourceDetails> mediaTypeDefn;

    public ResourceDefinitionEndPoint(EndpointContext<?> context, List<HasBookmarkAndUrl> companions) {
        this.register = EntityRegister.apply(companions);
        this.mediaTypeDefn = ResourceDetailsCompanion.companion.lensMediaDefn(context);
    }
    @Override public List<MethodAndPath> description() { return Lists.map(register.registered(), url -> new MethodAndPath("get", url)); }
    @Override public CompletableFuture<Optional<ServiceResponse>> apply(ServiceRequest serviceRequest) {
        return CompletableFuture.completedFuture(
                register.apply(new ResourceDetailsRequest(serviceRequest.path)).
                        map(rd -> new ServiceResponse(200, mediaTypeDefn.makeDataAndDefn(mediaTypeDefn.makeContextForJson(serviceRequest), s -> "", rd).asString(), List.of())));
    }
}
