package rip.osu.bancho.packet.impl.cho;

import rip.osu.bancho.packet.Packet;
import rip.osu.bancho.packet.util.ByteDataOutputStream;

import java.io.IOException;

public class ServerRebootedPacket extends Packet {
    public ServerRebootedPacket() {
        this.id = 86;
    }

    @Override
    public void write(ByteDataOutputStream stream) throws IOException {
        stream.writeInt(0)
                .dump(this.id);
    }
}
