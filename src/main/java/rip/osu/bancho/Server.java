package rip.osu.bancho;

import io.javalin.Javalin;
import io.javalin.http.ContentType;
import io.javalin.http.Context;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import rip.osu.bancho.model.LoginRequest;
import rip.osu.bancho.model.Player;
import rip.osu.bancho.mongo.Database;
import rip.osu.bancho.mongo.model.MongoPlayer;
import rip.osu.bancho.mongo.model.UserStatus;
import rip.osu.bancho.packet.impl.cho.*;
import rip.osu.bancho.packet.types.Channel;
import rip.osu.bancho.packet.util.ByteDataOutputStream;
import rip.osu.bancho.util.Constants;

import java.io.IOException;
import java.util.*;

@Slf4j(topic = "Server Main Thread")
public class Server {
    @Getter
    private static final List<Player> connectedPlayers = new ArrayList<>();
    private static final Map<String, Player> tokenToPlayer = new HashMap<>();
    private final List<Channel> channels = new ArrayList<>();

    public Server() {
        // init default channels
        channels.add(new Channel("#osu", "Click circles with osu!Rip members!", true));
        channels.add(new Channel("#off-topic", "Talk about random stuff.", true));
        channels.add(new Channel("#dev-talk", "Development talk.", false));

        Javalin httpServer = Javalin.create()
                .get("/", this::handleBrowserRequest)
                .post("/", this::handleOsuRequest)
                .get("/test", this::testDatabase)
                .before(this::ensureHeaders)
                .start(7070);
    }

    /**
     * Generates a random token that isn't currently in-use.
     *
     * @return The token.
     */
    public static String generateToken() {
        Random random = new Random();

        String generatedString;

        while (true) {
            generatedString = random.ints(48, 123)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                    .limit(32)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();

            if (tokenToPlayer.containsKey(generatedString)) continue;
            else break;
        }

        return generatedString;
    }

    private void testDatabase(Context context) {
        context.json(Database.getInstance().getScoreProfile(Database.getInstance().getPlayer(2).get()));
    }

    private void ensureHeaders(Context ctx) throws IOException {
        if (ctx.header("osu-token") == null) return;

        ctx.res().addHeader("cho-protocol", "19");

        if (!existsByToken(ctx.header("osu-token"))) {
            // handle server reboots
            System.out.println(ctx.header("osu-token") + " has sent a request, however we may have rebooted. Sending ServerRebooted!");
            new ServerRebootedPacket().write(new ByteDataOutputStream(ctx.res().getOutputStream()));
            ctx.skipRemainingHandlers();
        }
    }

    private void handleBrowserRequest(Context ctx) {
        ctx.contentType(ContentType.HTML)
                .result(Constants.BROWSER_REPLY);
    }

    private void handleOsuRequest(Context ctx) throws IOException {
        ctx.res().addHeader("cho-protocol", "19");
        ctx.res().setContentType("application/octet-stream");

        if (ctx.header("osu-token") == null) {
            // login time ;D
            log.info(ctx.body());

            LoginRequest req = new LoginRequest(ctx.body().split("\n"));

            if (Database.getInstance().getPlayer(req.getUsername()).isEmpty()) {
                new LoginReplyPacket(-1).write(
                        new ByteDataOutputStream(ctx.res().getOutputStream()));

                return;
            }

            MongoPlayer found = Database.getInstance().getPlayer(req.getUsername()).get();

            if (!found.getPasswordMD5().equals(req.getMd5Password())) {
                new LoginReplyPacket(-1).write(
                        new ByteDataOutputStream(ctx.res().getOutputStream()));

                return;
            }

            if (found.getUserStatus() != UserStatus.ACTIVE) {
                new LoginReplyPacket(-3).write(
                        new ByteDataOutputStream(ctx.res().getOutputStream()));

                return;
            }

            updatePlayerInfo(found, req);

            Player player = new Player(found,
                    Database.getInstance().getScoreProfile(found));
            connectPlayer(player);
            ctx.res().addHeader("cho-token", player.getToken());

            new LoginReplyPacket(found.getId()).write(
                    new ByteDataOutputStream(ctx.res().getOutputStream()));

            queueLoginPackets(player);

            return;
        }

        Player player = tokenToPlayer.get(ctx.header("osu-token"));

        // TODO: handle osu packets
        log.info("Flushing queue for {}!", player.getPlayer().getUsername());
        player.dumpQueue(ctx.res().getOutputStream());

        ctx.res().getOutputStream().flush();
        ctx.res().flushBuffer();
    }

    private void queueLoginPackets(Player player) {
        player.queuePacket(new UserStatsPacket().setContext(player))
                .queuePacket(new UserPresencePacket().setContext(player))
                .queuePacket(new FriendsListPacket().setContext(player))
                .queuePacket(new LoginPermissionsPacket().setContext(player));

        for (Channel channel : channels) {
            if (channel.isAutoJoin())
                player.queuePacket(new ChannelAutoJoinPacket().setContext(channel));
            else player.queuePacket(new ChannelInfoPacket().setContext(channel));
        }

        player.queuePacket(new ChannelInfoEndPacket());

        for (Player p : connectedPlayers) {
            if(p.getPlayer().getId() == player.getPlayer().getId())
                return;

            player.queuePacket(new UserStatsPacket().setContext(p));
            player.queuePacket(new UserPresencePacket().setContext(p));
        }

        player.queuePacket(new NotificationPacket("she output on my stream till i flush"))
                .queuePacket(new NotificationPacket(":3 - Running Wowie Alpha | Development Build"))
                .toggleProcessing();
    }

    private void updatePlayerInfo(MongoPlayer found, LoginRequest req) {
        found.setUtcOffset(req.getUtcOffset());
        Database.getInstance().updatePlayer(found);
    }

    /**
     * Adds players to the correct lists
     */
    private void connectPlayer(Player player) {
        tokenToPlayer.put(player.getToken(), player);
        connectedPlayers.add(player);
    }

    /**
     * Check if a Player is connected via their token.
     *
     * @param token The osu-token header.
     * @return Whether the player is connected or not.
     */
    public boolean existsByToken(String token) {
        return tokenToPlayer.containsKey(token);
    }
}
