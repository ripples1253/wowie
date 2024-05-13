package rip.osu.bancho.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import rip.osu.bancho.mongo.model.MongoPlayer;
import rip.osu.bancho.mongo.model.ScoreProfile;
import rip.osu.bancho.util.Constants;

import java.util.Optional;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Database {
    private static Database instance;
    private final MongoClient client;
    private final MongoDatabase db;
    private final MongoCollection<MongoPlayer> players;
    private final MongoCollection<ScoreProfile> scoreProfile;

    private Database() {

        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                pojoCodecRegistry);

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(Constants.MONGO_URI))
                .codecRegistry(codecRegistry)
                .build();

        this.client = MongoClients.create(settings);
        this.db = client.getDatabase("wowie");
        this.players = db.getCollection("players", MongoPlayer.class);
        this.scoreProfile = db.getCollection("score_profile", ScoreProfile.class);
    }

    public static Database getInstance() {
        if (instance == null)
            instance = new Database();

        return instance;
    }

    public Optional<MongoPlayer> getPlayer(int id) {
        return Optional.ofNullable(players.find(Filters.eq("_id", id)).first());
    }

    public Optional<MongoPlayer> getPlayer(String username) {
        return Optional.ofNullable(players.find(Filters.eq("username", username)).first());
    }

    public Optional<MongoPlayer> getPlayerByEmail(String email) {
        return Optional.ofNullable(players.find(Filters.eq("email", email)).first());
    }

    public void updatePlayer(MongoPlayer player) {
        players.replaceOne(Filters.eq("_id", player.getId()), player);
    }

    public ScoreProfile getScoreProfile(MongoPlayer player) {
        return scoreProfile.find(Filters.eq("_id", player.getId())).first();
    }
}
