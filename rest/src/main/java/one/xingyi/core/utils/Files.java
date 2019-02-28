package one.xingyi.core.utils;
import java.io.*;
import java.util.concurrent.Callable;
public class Files {

    public static void setText(Callable<PrintWriter> writer, String text) {
        WrappedException.wrap(() -> {
            try (PrintWriter printWriter = writer.call()) {
                printWriter.print(text);
                printWriter.flush();
            }
        });
    }

    public static String getText(String name) {
        return WrappedException.wrapCallable(() -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Files.class.getClassLoader().getResourceAsStream(name)));
            StringBuilder result = new StringBuilder();
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }

            } finally {
                reader.close();
            }
            return result.toString();
        });
    }

    public static String getText(File file) {
        return WrappedException.wrapCallable(() -> {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder result = new StringBuilder();
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }

            } finally {
                reader.close();
            }
            return result.toString();
        });
    }
}
