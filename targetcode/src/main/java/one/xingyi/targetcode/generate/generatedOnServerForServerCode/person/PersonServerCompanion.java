package one.xingyi.targetcode.generate.generatedOnServerForServerCode.person;
import one.xingyi.core.javascript.JavascriptStore;
import one.xingyi.core.sdk.IXingYiServerCompanion;
import one.xingyi.targetcode.generate.generatedOnServerForServerDevelopers.person.Person;
import one.xingyi.targetcode.manuallyDefinedOnServer.person.IPersonEntityDefn;

import java.util.LinkedHashMap;
import java.util.Map;
public class PersonServerCompanion implements IXingYiServerCompanion<IPersonEntityDefn, Person> {
    @Override public String bookmark() { return "person"; }
    @Override public JavascriptStore javascriptStore() { return null; }

    public Map<String, String> javascriptMap() {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("IPerson_name_String", "function lens_IPerson_name_String(){ return lens('name');};");
        return result;
    }

}
