package one.xingyi.core.server;
import java.io.*;
public class Streams {

   public static String readAll(InputStream stream) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));
        try {
            String line = null;
            do {
                line = in.readLine();
                result.append(line);
                result.append('\n');
            } while (line != null);
        } finally {in.close();}
        return result.toString();
    }


    public static void sendAll(OutputStream stream, byte[] bytes) throws IOException {
        try {
            stream.write(bytes);
        } finally {stream.close();}
    }
}
