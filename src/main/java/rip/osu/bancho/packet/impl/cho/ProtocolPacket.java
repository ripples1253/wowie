package rip.osu.bancho.packet.impl.cho;

import rip.osu.bancho.packet.Packet;
import rip.osu.bancho.packet.util.ByteDataOutputStream;

import java.io.IOException;

public class ProtocolPacket extends Packet {
    public ProtocolPacket() {
        this.id = 75;
    }

    @Override
    public void write(ByteDataOutputStream stream) throws IOException {
        stream.writeInt(19)
                .dump(this.id);
    }
}
