package rip.osu.bancho.packet.types;

import lombok.Getter;

public enum PresenceAction {
    Idle(0),
    Afk(1),
    Playing(2),
    Editing(3),
    Modding(4),
    Multiplayer(5),
    Watching(6),
    Unknown(7),
    Testing(8),
    Submitting(9),
    Paused(10),
    Lobby(11),
    Multiplaying(12),
    OsuDirect(13);

    @Getter
    private final int status;

    PresenceAction(int status) {
        this.status = status;
    }
}
