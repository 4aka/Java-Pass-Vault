package be.sec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashPass {

    /**
     * TODO Hardcoded salt. Is there any way to avoid hardcode?
     */
    private final byte[] salt = { 7,-117,-127,-37,-87,-127,-123,-102,-62,-20,-32,-41,-117,-99,-30,-43,-14,-124,-24,
            -55,-120,-67,-71,-83,-22,-34,-63,-68,-26,-51,-6,-81,-108,-64,-33,-3,-115,-110,-111,-60,-74,-111,-53,-85,
            -84,-91,-27,-38,-118,-91,-25,-50,-46,-117,-82,-47,-34,-31,-40,-45,-100,-42,-45,-74,-36,-39,-52,-66,-16,
            -84,-91,-27,-38,-118,-91,-25,-50,-46,-117,-82,-47,-34,-31,-40,-45,-100,-42,-45,-74,-36,-39,-52,-66,-16,
            -84,-91,-27,-38,-118,-91,-25,-50,-46,-117,-82,-47,-34,-31,-40,-45,-100,-42,-45,-74,-36,-39,-52,-66,-16,
            -55,-120,-67,-71,-83,-22,-34,-63,-68,-26,-51,-6,-81,-108,-64,-33,-3,-115,-110,-111,-60,-74,-111,-53,-85,
            -55,-120,-67,-71,-83,-22,-34,-63,-68,-26,-51,-6,-81,-108,-64,-33,-3,-115,-110,-111,-60,-74,-111,-53,-85,
            -47,-71,-86,-94,-78,-59,-95,-22,-71,-52,-13,-49,-28,-20,-89,-47,-90,-36,-6,-68,-4,-103,-41,-24,-110 };

    /**
     * @param password
     * @param salt
     * @return secured password
     */
    private String getSecurePassword(String password, byte[] salt) {
        String generatedPassword = null;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                // TODO What are these digits???
                // sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {e.printStackTrace();}

        return generatedPassword;
    }

    /**
     * @param password
     * @return
     */
    public String hashPassword(String password) {
        String result = "";
        for (int i = 0; i < 10; i++) result = getSecurePassword(password, salt);
        return result;
    }
}
