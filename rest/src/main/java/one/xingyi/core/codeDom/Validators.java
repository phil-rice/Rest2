package one.xingyi.core.codeDom;
import one.xingyi.core.annotationProcessors.ElementFail;
import one.xingyi.core.validation.Result;

import static one.xingyi.core.validation.Valid.check;
public class Validators {

    public static Result<String, String> removeIAndDefn(String prefix, String raw) {
        return Result.<String, String>validate(raw,
                check(i -> i.startsWith("I"), i -> prefix + " [" + i + "] Should start with an I"),
                check(i -> i.endsWith("Defn"), i -> prefix + " [" + i + "] Should end with Defn")
        ).map(s -> s.substring(1, s.length() - 4));
    }
}
