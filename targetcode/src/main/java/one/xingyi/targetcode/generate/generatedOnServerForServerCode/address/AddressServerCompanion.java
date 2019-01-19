package one.xingyi.targetcode.generate.generatedOnServerForServerCode.address;
import one.xingyi.core.javascript.JavascriptStore;
import one.xingyi.core.sdk.IXingYiServerCompanion;
import one.xingyi.targetcode.generate.generatedOnServerForServerDevelopers.address.Address;
import one.xingyi.targetcode.manuallyDefinedOnServer.address.IAddressEntityDefn;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
public class AddressServerCompanion implements IXingYiServerCompanion<IAddressEntityDefn, Address> {
    @Override public Optional<String> bookmark() { return Optional.of("/address"); }
//    @Override public JavascriptStore javascriptStore() { return null; }

    public Map<String, String> javascriptMap() {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("IAddress_line1_String", "function lens_IAddress_line1_String(){ return lens('line1');};");
        result.put("IAddress_line2_String", "function lens_IAddress_line2_String(){ return lens('line2');};");
        return result;
    }
}
