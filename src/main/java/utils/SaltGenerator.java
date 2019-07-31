package utils;

public class SaltGenerator {

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String getSalt() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public static String saltPassword(String password, String salt) {
        int mid = salt.length() / 2;
        String[] parseSalt = {salt.substring(0, mid), salt.substring(mid)};
        return parseSalt[0] + password + parseSalt[1];
    }
}
