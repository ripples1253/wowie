package rip.osu.bancho.packet.types;

import lombok.Getter;

import java.util.List;

@Getter
public enum Permissions {
    Unrestricted(1 << 0),
    Verified(1 << 1),
    Whitelisted(1 << 2),
    Supporter(1 << 4),
    Premium(1 << 5),
    Alumni(1 << 7),
    TourneyManager(1 << 10),
    Nominator(1 << 11),
    Moderator(1 << 12),
    Administrator(1 << 13),
    Developer(1 << 14);

    private final int id;

    Permissions(int id) {
        this.id = id;
    }

    public int calculatePermissions(List<Permissions> perms) {
        int i = 0;

        for (Permissions perm : perms) {
            i = i | perm.getId();
        }

        return i;
    }
}
