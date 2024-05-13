package rip.osu.bancho.packet.impl.cho;

import rip.osu.bancho.packet.Packet;
import rip.osu.bancho.packet.util.ByteDataOutputStream;

import java.io.IOException;

public class LoginReplyPacket extends Packet {
    private final int status;

    public LoginReplyPacket(int status) {
        this.id = 5;
        this.status = status;
    }

    @Override
    public void write(ByteDataOutputStream stream) throws IOException {
        stream.writeInt(status)
                .dump(this.id);
    }
}