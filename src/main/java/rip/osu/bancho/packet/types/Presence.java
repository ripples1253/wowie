package rip.osu.bancho.packet.types;

import lombok.Getter;
import rip.osu.bancho.model.Player;

@Getter
public class Presence {
    private final int id;
    private final String username;
    private final int utcOffset;
    private final int countryCode;
    private final int perms;
    private final float longitude;
    private final float latitude;
    private final int rank;

    public Presence(Player user, int utcOffset, int countryCode,
                    int perms, float longitude, float latitude,
                    int rank) {
        this.id = user.getPlayer().getId();
        this.username = user.getPlayer().getUsername();
        this.countryCode = countryCode;
        this.utcOffset = utcOffset;
        this.perms = perms;
        this.longitude = longitude;
        this.latitude = latitude;
        this.rank = rank;
    }
}
