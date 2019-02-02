package one.xingyi.core.utils;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.function.Function;
public interface Digestor extends Function<String, DigestAndString> {
    public final ThreadLocal<Digestor> digestor = new ThreadLocal<>() {
        @Override protected Digestor initialValue() {
            return Digestor.sha256();
        }
    };
    public static Digestor digestor() {return digestor.get();}

    public static Digestor sha256() {
        return new Digestor() {
            MessageDigest digest = WrappedException.wrapCallable(() -> MessageDigest.getInstance("SHA-256"));
            @Override public DigestAndString apply(String s) {
                return new DigestAndString(Base64.getEncoder().encodeToString(digest.digest(s.getBytes(StandardCharsets.UTF_8))), s);
            }
        };
    }
}
