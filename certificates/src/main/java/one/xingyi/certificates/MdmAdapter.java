package one.xingyi.certificates;
import one.xingyi.certificates.server.controller.ICertificateController;
import one.xingyi.certificates.server.domain.Certificate;
import one.xingyi.core.utils.IdAndValue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
public class MdmAdapter implements ICertificateController {
    @Override public String stateFn(Certificate entity) {
        return "";
    }


    Map<String, Certificate> map = Collections.synchronizedMap(new HashMap<>());

    public MdmAdapter() {
        map.put("id1", new Certificate("id1"));
    }
    @Override public CompletableFuture<Certificate> put(IdAndValue<Certificate> idAndCertificate) {
        return null;
    }
    @Override public CompletableFuture<Optional<Certificate>> getOptional(String id) {
        return CompletableFuture.completedFuture(Optional.ofNullable(map.get(id)));
    }
    @Override public CompletableFuture<Boolean> delete(String id) {
        return null;
    }
    @Override public CompletableFuture<Certificate> create(String id) {
        return null;
    }
    @Override public CompletableFuture<IdAndValue<Certificate>> create() {
        return null;
    }
}
