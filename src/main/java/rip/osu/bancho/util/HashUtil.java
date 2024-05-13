package rip.osu.bancho.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 Hashing Utilities
 *
 * @author ripples1253
 */
public class HashUtil {

    /**
     * Generates an MD5 hash.
     *
     * @param str The String to hash.
     * @return The generated MD5 hash.
     */
    public String generateHash(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] messageDigest = md.digest(str.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;
        }

        // this is literally never thrown. stupid API design tbh
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
