package rip.osu.bancho.packet.impl.cho;

import rip.osu.bancho.model.Player;
import rip.osu.bancho.packet.Packet;
import rip.osu.bancho.packet.util.ByteDataOutputStream;

import java.io.IOException;

public class FriendsListPacket extends Packet {
    public FriendsListPacket() {
        this.id = 72;
    }

    @Override
    public void write(ByteDataOutputStream stream) throws IOException {
        Player p = (Player) this.ctx;

        stream.writeIntList(p.getPlayer().getFriends())
                .dump(this.id); // this'll be zero anyway, we're osu! players.
    }
}
