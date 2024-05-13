package rip.osu.bancho.packet.impl.cho;

import rip.osu.bancho.packet.Packet;
import rip.osu.bancho.packet.types.Channel;
import rip.osu.bancho.packet.util.ByteDataOutputStream;

import java.io.IOException;

public class ChannelInfoPacket extends Packet {
    public ChannelInfoPacket() {
        this.id = 65;
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