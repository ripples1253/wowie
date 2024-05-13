package rip.osu.bancho.util;

import rip.osu.bancho.Server;

import java.io.IOException;

public class Constants {
    public static double WOWIE_VERSION = 1.0;

    public static String BROWSER_REPLY;
    public static String MONGO_URI = System.getenv("wowie_mongo-uri");

    static {
        try {
            BROWSER_REPLY = String.format(ResourcesUtil.readFile("index.html"),
                    WOWIE_VERSION,
                    Server.getConnectedPlayers().size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
