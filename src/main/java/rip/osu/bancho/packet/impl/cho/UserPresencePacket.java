package rip.osu.bancho.packet.impl.cho;

import rip.osu.bancho.model.Player;
import rip.osu.bancho.packet.Packet;
import rip.osu.bancho.packet.types.Presence;
import rip.osu.bancho.packet.util.ByteDataOutputStream;

import java.io.IOException;

public class UserPresencePacket extends Packet {

    public UserPresencePacket() {
        this.id = 83;
    }

    @Override
    public void write(ByteDataOutputStream stream) throws IOException {
        Player p = (Player) this.ctx;
        Presence presence = p.getPresence();

        // ---bUserPresence Struct START---
        stream.writeInt(presence.getId())
                .writeString(presence.getUsername())
                .writeByte((byte) ((byte) presence.getUtcOffset() + 24))
                .writeInt(presence.getCountryCode())
                .writeByte((byte) (presence.getPerms() | p.getStatus().getMode().getId() << 5))
                .writeFloat(presence.getLongitude())
                .writeFloat(presence.getLatitude())
                .writeInt(presence.getRank())
                .dump(this.id);
        // ---bUserPresence Struct END---
    }
}
