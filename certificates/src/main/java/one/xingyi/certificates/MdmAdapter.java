package one.xingyi.certificates;
import one.xingyi.certificates.server.controller.ICertificateController;
import one.xingyi.certificates.server.domain.Certificate;
import one.xingyi.core.store.ControllerUsingMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
public class MdmAdapter extends ControllerUsingMap<Certificate> implements ICertificateController {
    @Override public String stateFn(Certificate entity) {
        return "";
    }


    Map<String, Certificate> map = Collections.synchronizedMap(new HashMap<>());

    public MdmAdapter() {
        super("Certificate");
        map.put("id1", new Certificate("id1"));
    }
    @Override protected Certificate prototype(String id) { return new Certificate(id); }
}
