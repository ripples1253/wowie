package rip.osu.bancho.packet;

import rip.osu.bancho.packet.impl.cho.LoginPermissionsPacket;
import rip.osu.bancho.packet.impl.cho.LoginReplyPacket;
import rip.osu.bancho.packet.impl.cho.ServerRebootedPacket;
import rip.osu.bancho.packet.impl.cho.UserStatsPacket;

public enum Packets {
    Cho_LoginReply(5, LoginReplyPacket.class),
    Cho_UserStats(11, UserStatsPacket.class),
    Cho_LoginPermissions(71, LoginPermissionsPacket.class),
    Cho_ServerRebooted(86, ServerRebootedPacket.class);

    private final int id;
    private final Class<? extends Packet> clazz;

    Packets(int id, Class<? extends Packet> clazz) {
        this.id = id;
        this.clazz = clazz;
    }
}
