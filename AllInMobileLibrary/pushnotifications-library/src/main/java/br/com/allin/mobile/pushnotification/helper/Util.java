package br.com.allin.mobile.pushnotification.helper;

import android.content.Context;
import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.allin.mobile.pushnotification.identifiers.ConfigIdentifier;

/**
 * Class that contains common methods that will be used throughout the library.
 */
public class Util {

    /**
     * Private constructor to prevent the class is instantiated.
     */
    private Util() {
    }

    /**
     * Checks whether the string value is null or empty string.
     *
     * @param value String to be checked.
     *
     * @return If the string value is null or empty, returns {@code true}.
     * Otherwise, returns {@code false}.
     */
    public static boolean isEmpty(String value) {
        return TextUtils.isEmpty(value.trim());
    }

    /**
     * Returns the AllIn token saved in the
     * key {@code all_in_token}. The token must be informed in the XML string.
     *
     * @param context Application context.
     *
     * @return String with the value of the project in AllIn token.
     */
    public static String getToken(Context context) {
        int tokenId = context.getResources()
                .getIdentifier(ConfigIdentifier.TOKEN, "string", context.getPackageName());

        if (tokenId > 0) {
            return context.getString(tokenId);
        } else {
            return null;
        }
    }

    /**
     * For questions regarding the MD5
     * MD5 <a href="https://pt.wikipedia.org/wiki/MD5">https://pt.wikipedia.org/wiki/MD5</a>
     *
     * @param value Value that will be generated MD5
     *
     * @return MD5 value
     */
    public static String md5(String value) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(value.getBytes());
            byte[] messageDigest = digest.digest();

            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);

                while (h.length() < 2) {
                    h = "0".concat(h);
                }

                hexString.append(h);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * @param format Data forma (ex: "yyyy-MM-dd HH:mm:ss")
     *
     * @return the date according to the format
     */
    public static String currentDate(String format) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(new Date());
    }
}
