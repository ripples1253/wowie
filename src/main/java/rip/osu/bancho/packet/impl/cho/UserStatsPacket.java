package rip.osu.bancho.packet.impl.cho;

import rip.osu.bancho.model.Player;
import rip.osu.bancho.packet.Packet;
import rip.osu.bancho.packet.util.ByteDataOutputStream;

import java.io.IOException;

public class UserStatsPacket extends Packet {
    public UserStatsPacket() {
        this.id = 11;
    }

    @Override
    public void write(ByteDataOutputStream stream) throws IOException {
        Player p = (Player) this.ctx;

        stream.writeInt(p.getPlayer().getId())
                // --- bStatusUpdate Struct START ---
                .writeByte((byte) p.getStatus().getAction().getStatus())
                .writeString(p.getStatus().getActionText())
                .writeString(p.getStatus().getMapMD5())
                .writeInt(p.getStatus().calculateMods())
                .writeByte((byte) p.getStatus().getMode().getId())
                .writeInt(p.getStatus().getMapId())
                // --- bStatusUpdate Struct END ---
                .writeLong(p.getStatus().getRankedScore())
                .writeFloat(p.getStatus().getAccuracy() / 100) // BUG: osu! overflows to 9 quintillion LMFAO
                .writeInt(p.getStatus().getPlayCount())
                .writeLong(p.getStatus().getTotalScore())
                .writeInt(p.getStatus().getRank())
                .writeShort(p.getStatus().getPp())
                .dump(this.id);
    }
}
