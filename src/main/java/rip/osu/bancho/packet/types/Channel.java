package rip.osu.bancho.packet.types;

import lombok.Getter;
import rip.osu.bancho.model.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Channel {
    private final String name;
    private final String topic;
    private final List<Player> members;
    private final boolean autoJoin;

    public Channel(String name, String topic, boolean autoJoin) {
        this.name = name;
        this.topic = topic;
        this.members = new ArrayList<>();
        this.autoJoin = autoJoin;
    }

    public void addPlayer(Player player) {
        members.add(player);
    }

    public void removePlayer(Player player) {
        members.remove(player);
    }

    private int getPlayerCount() {
        return members.size();
    }
}
