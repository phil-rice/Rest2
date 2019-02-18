package one.xingyi.certificates;
import one.xingyi.certificates.server.controller.ICertificateController;
import one.xingyi.certificates.server.domain.Certificate;
import one.xingyi.certificates.server.domain.Details;
import one.xingyi.core.store.ControllerUsingMap;
public class MdmAdapter extends ControllerUsingMap<Certificate> implements ICertificateController {
    @Override public String stateFn(Certificate entity) {
        return "";
    }

    public MdmAdapter() {
        super("Certificate");
        store.put("id1", prototype("id1"));
    }
//    @Override protected Certificate prototype(String id) { return new Certificate(id); }
    @Override protected Certificate prototype(String id) { return new Certificate(new Details(id)); }
}
