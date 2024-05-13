package rip.osu.bancho.mongo.model;

import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonProperty;
import rip.osu.bancho.packet.types.Permissions;

import java.util.List;

@Data
public class MongoPlayer {
    @BsonProperty("_id")
    private Integer id;

    private String email;
    private String username;

    @BsonProperty("password_md5")
    private String passwordMD5;

    @BsonProperty("friend_ids")
    private List<Integer> friends;

    @BsonProperty("user_status")
    private UserStatus userStatus;

    @BsonProperty("utc_offset")
    private Integer utcOffset;

    @BsonProperty("country_code")
    private Integer countryCode;

    private Integer perms;
    private Integer rank;

    private Integer permissions;

    @BsonProperty("score_profile_id")
    private Integer scoreProfileId;

    public void addPermission(Permissions perm) {
        permissions = permissions | perm.getId();
    }
}
