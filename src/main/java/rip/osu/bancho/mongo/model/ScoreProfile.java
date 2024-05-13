package rip.osu.bancho.mongo.model;

import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Data
public class ScoreProfile {
    @BsonProperty("_id")
    private int id;

    @BsonProperty("ranked_score")
    private int rankedScore;
    @BsonProperty("total_score")
    private int totalScore;

    @BsonProperty("accuracy")
    private float accuracy;

    @BsonProperty("play_count")
    private int playCount;

    @BsonProperty("rank")
    private int rank;
    @BsonProperty("pp")
    private short pp;

    @BsonCreator
    public ScoreProfile(@BsonProperty("_id") int id,
                        @BsonProperty("ranked_score") int rankedScore,
                        @BsonProperty("total_score") int totalScore,
                        @BsonProperty("accuracy") float accuracy,
                        @BsonProperty("play_count") int playCount,
                        @BsonProperty("rank") int rank,
                        @BsonProperty("pp") short pp) {
        this.id = id;
        this.rankedScore = rankedScore;
        this.totalScore = totalScore;
        this.accuracy = accuracy;
        this.playCount = playCount;
        this.rank = rank;
        this.pp = pp;
    }
}
