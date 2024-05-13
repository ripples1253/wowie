package rip.osu.bancho.packet.types;

import lombok.Getter;

public enum GameMode {
    OSU(0),
    MANIA(1),
    TAIKO(2),
    CTBN(3);

    @Getter
    private final int id;

    GameMode(int id) {
        this.id = id;
    }
}
