package rip.osu.bancho.packet.impl.cho;

import rip.osu.bancho.model.Player;
import rip.osu.bancho.packet.Packet;
import rip.osu.bancho.packet.util.ByteDataOutputStream;

import java.io.IOException;

public class LoginPermissionsPacket extends Packet {
    public LoginPermissionsPacket() {
        this.id = 71;
    }

    @Override
    public void write(ByteDataOutputStream stream) throws IOException {
        Player p = (Player) this.ctx;
        stream.writeInt(p.getPlayer().getPermissions())
                .dump(this.id);
    }
}
