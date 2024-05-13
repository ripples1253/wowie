package rip.osu.bancho.packet.impl.cho;

import rip.osu.bancho.packet.Packet;
import rip.osu.bancho.packet.types.Channel;
import rip.osu.bancho.packet.util.ByteDataOutputStream;

import java.io.IOException;

public class ChannelAutoJoinPacket extends Packet {
    public ChannelAutoJoinPacket() {
        this.id = 67;
    }

    @Override
    public void write(ByteDataOutputStream stream) throws IOException {
        Channel channel = (Channel) this.ctx;
        stream.writeString(channel.getName())
                .writeString(channel.getTopic())
                .writeInt(channel.getMembers().size())
                .dump(this.id);
    }
}
