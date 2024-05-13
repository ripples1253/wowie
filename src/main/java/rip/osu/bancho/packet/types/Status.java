package rip.osu.bancho.packet.types;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import rip.osu.bancho.mongo.model.ScoreProfile;

import java.util.Collections;
import java.util.List;

@Data
@Slf4j(topic = "Status Writer")
public class Status {
    private PresenceAction action;
    private String actionText;
    private String mapMD5;
    private List<Mods> mods; // when sent to the client, this needs to be calculated.
    private GameMode mode;
    private int mapId;
    private int rankedScore;
    private float accuracy;
    private int playCount;
    private int totalScore;
    private int rank;
    private short pp;

    public Status(ScoreProfile profile) {
        this.action = PresenceAction.Idle;
        this.actionText = "on the Wowie Developer Preview";
        this.mapMD5 = "";
        this.mods = Collections.singletonList(Mods.NOMOD);
        this.mode = GameMode.OSU;
        this.mapId = 0;
        this.rankedScore = profile.getRankedScore();
        this.accuracy = profile.getAccuracy();
        this.playCount = profile.getPlayCount();
        this.totalScore = profile.getTotalScore();
        this.rank = profile.getRank();
        this.pp = profile.getPp();

        log.info("""
                Ranked Score: {}
                Accuracy: {}
                Play Count: {}
                Total Score: {}
                Rank: {}
                PP: {}
                """, rankedScore, accuracy / 100, playCount, totalScore, rank, pp);
    }

    public int calculateMods() {
        int i = 0;

        for (Mods mod : mods) {
            i = i | mod.getId();
        }

        return i;
    }
}
