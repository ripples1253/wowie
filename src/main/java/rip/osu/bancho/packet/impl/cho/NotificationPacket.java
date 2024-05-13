package rip.osu.bancho.packet.impl.cho;

import rip.osu.bancho.packet.Packet;
import rip.osu.bancho.packet.util.ByteDataOutputStream;

import java.io.IOException;

public class NotificationPacket extends Packet {
    private final String message;

    public NotificationPacket(String message) {
        this.id = 24;
        this.message = message;
    }

    @Override
    public void write(ByteDataOutputStream stream) throws IOException {
        stream.writeString(message)
                .dump(this.id);
    }
}
