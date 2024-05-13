package rip.osu.bancho.model;

import lombok.Getter;

/**
 * This is awful, but Peppy has forced my hands.
 *
 * @apiNote wowie, this is bad!
 */
@Getter
public class LoginRequest {
    private final String username;

    private final String md5Password;

    private final String version;
    private final Integer utcOffset;
    private final boolean locationDisplayed;
    private final String[] macAddresses;
    private final boolean privatePMs;

    public LoginRequest(String[] body) {
        this.username = body[0];

        this.md5Password = body[1];

        // line 3, this is where the fuckery comes in...
        String[] settings = body[2].split("\\|");
        this.version = settings[0];
        this.utcOffset = Integer.parseInt(settings[1]);
        this.locationDisplayed = Boolean.parseBoolean(settings[2]);
        this.macAddresses = settings[3].split(":");
        this.privatePMs = Boolean.parseBoolean(settings[4]);
    }
}
