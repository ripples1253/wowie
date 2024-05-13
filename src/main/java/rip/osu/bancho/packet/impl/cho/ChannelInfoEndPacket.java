package rip.osu.bancho.packet.impl.cho;

import rip.osu.bancho.packet.Packet;
import rip.osu.bancho.packet.util.ByteDataOutputStream;

import java.io.IOException;

public class ChannelInfoEndPacket extends Packet {
    public ChannelInfoEndPacket() {
        this.id = 89;
    }

    @Override
    public void write(ByteDataOutputStream stream) throws IOException {
        stream.dump(this.id);
    }
}
