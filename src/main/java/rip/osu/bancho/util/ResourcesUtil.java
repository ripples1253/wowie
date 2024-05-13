package rip.osu.bancho.util;

import java.io.IOException;
import java.io.InputStream;

public class ResourcesUtil {
    public static String readFile(String name) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(name);

        if (is == null) throw new IOException("Resources file doesn't exist! Name: " + name);

        return new String(is.readAllBytes());
    }
}
