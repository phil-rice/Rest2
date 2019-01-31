package one.xingyi.core.utils;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
public class Strings {
    public static String getFrom(Consumer<PrintStream> consumer) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        consumer.accept(new PrintStream(stream));
        return stream.toString();
    }

    // pad with " " to the right to the given length (n)
    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }
    // pad with " " to the left to the given length (n)
    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }
    public static String noWhitespace(String s) {return s.replaceAll("\\se*", ""); }
    public static String changeQuotes(String s) {return s.replace('\'', '"'); }

    public static Function<String, Optional<String>> ripIdFromPath(String bookmark) {
        int index = bookmark.indexOf("{id}");
        if (index == -1) throw new IllegalArgumentException("Bookmark: " + bookmark + " is invalid");
        String startString = bookmark.substring(0, index);
        String endString = bookmark.substring(index + 4);
        return path -> {
            if (path.startsWith(startString) && path.endsWith(endString)) {
                String substring = path.substring(startString.length(), path.length() - endString.length());
                if (substring.indexOf("/") != -1) return Optional.empty();
                return Optional.of(substring);
            }
            return Optional.empty();
        };
    }


    public static Optional<String> from(String s) { return Optionals.from(s != null && s.length() > 0, () -> s); }
    public static String from(String s, String defaultValue) { return s != null && s.length() > 0 ? s : defaultValue; }
    public static String lift(String monad, String inside) {return monad + "<" + inside + ">";}
    public static List<String> useIf(boolean b, String string) { return b ? Arrays.asList(string) : Arrays.asList();}

    public static final String[] empty = new String[0];
    public static String quote(String s) { return "\"" + s + "\"";}

    public static String removeOptionalFirst(String first, String value) {
        if (value.startsWith(first))
            return value.substring(first.length());
        else return value;
    }

    public static String removeOptionalLast(String last, String value) {
        if (value.endsWith(last))
            return value.substring(0, value.length() - last.length());
        return value;
    }

    public static String extractFromOptionalEnvelope(String start, String end, String s) {
        int startIndex = s.indexOf(start);
        int endOfStart = startIndex + start.length();
        int endIndex = s.indexOf(end, endOfStart);
        if (startIndex == -1 || endIndex == -1)
            return s;
        return s.substring(endOfStart, endIndex);
    }

    public static String lastSegement(String separator, String s) {
        String[] split = s.split(separator);
        return split.length == 0 ? "" : split[split.length - 1];
    }
    public static String allButLastSegment(String separator, String s) {
        int index = s.lastIndexOf(separator);
        if (index == -1) return "";
        else return s.substring(0, index);
    }
    public static String firstLetterUppercase(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
    public static boolean hasContent(String s) {
        return s != null && s.length() > 0;
    }
}
