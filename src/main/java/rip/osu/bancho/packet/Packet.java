package rip.osu.bancho.packet;

import org.jetbrains.annotations.Nullable;
import rip.osu.bancho.model.Player;
import rip.osu.bancho.packet.util.ByteDataInputStream;
import rip.osu.bancho.packet.util.ByteDataOutputStream;

import java.io.IOException;

/**
 * This is how we'll handle packets.
 * Yes, it's stupid. No, I don't care <3
 */
public abstract class Packet {
    public short id;
    public Object ctx;

    public Packet setContext(Object ctx) {
        this.ctx = ctx;
        return this;
    }

    /**
     * Handle this packet, user is null if their token is invalid (needed for some, e.g. ServerRebooted)
     *
     * @param stream The ByteDataInputStream, so we can read the packet.
     * @param ctx    The player, if their token is valid.
     */
    public void handle(ByteDataInputStream stream,
                       @Nullable Player ctx) {
    }

    /**
     * Write this packet's data.
     *
     * @param stream The ByteDataOutputStream, so we can write the packet.
     */
    public void write(ByteDataOutputStream stream) throws IOException {
    }
}
