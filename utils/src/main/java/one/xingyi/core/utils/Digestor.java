package one.xingyi.core.utils;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.function.Function;
public interface Digestor extends Function<String, String> {

    public static Digestor sha256() {
        return new Digestor() {
            MessageDigest digest = WrappedException.wrapCallable(() -> MessageDigest.getInstance("SHA-256"));
            @Override public String apply(String s) {
                return Base64.getEncoder().encodeToString(digest.digest(s.getBytes(StandardCharsets.UTF_8)));
            }
        };
    }
}
