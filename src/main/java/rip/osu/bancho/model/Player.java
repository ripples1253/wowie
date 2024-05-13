package rip.osu.bancho.model;

import lombok.Getter;
import rip.osu.bancho.Server;
import rip.osu.bancho.mongo.model.MongoPlayer;
import rip.osu.bancho.mongo.model.ScoreProfile;
import rip.osu.bancho.packet.Packet;
import rip.osu.bancho.packet.types.Presence;
import rip.osu.bancho.packet.types.Status;
import rip.osu.bancho.packet.util.ByteDataOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Player {
    @Getter
    private final MongoPlayer player;

    @Getter
    private final String token; // cho-token header

    @Getter
    private final Status status;
    @Getter
    private final Presence presence;
    private final Queue<Packet> packetQueue;
    private boolean pauseProcessing;

    public Player(MongoPlayer player, ScoreProfile scoreProfile) {
        this.player = player;
        this.token = Server.generateToken();
        this.packetQueue = new ConcurrentLinkedQueue<>();

        this.status = new Status(scoreProfile);
        this.presence = new Presence(this, player.getUtcOffset(), player.getCountryCode(),
                player.getPerms(), 0, 0, player.getRank());
        this.pauseProcessing = true;
    }

    public Player queuePacket(Packet p) {
        packetQueue.add(p);
        return this;
    }

    /**
     * Pause processing elements in the packet queue, not used for much besides login tbh
     */
    public void toggleProcessing() {
        this.pauseProcessing = !this.pauseProcessing;
    }

    public synchronized void dumpQueue(OutputStream outputStream) throws IOException {
        if (pauseProcessing) return;
        for (Packet p : packetQueue) {
            ByteArrayOutputStream temp = new ByteArrayOutputStream();
            ByteDataOutputStream tempStream = new ByteDataOutputStream(temp);

            p.write(tempStream);
            outputStream.write(temp.toByteArray());
            packetQueue.remove(p);
        }
    }
}
