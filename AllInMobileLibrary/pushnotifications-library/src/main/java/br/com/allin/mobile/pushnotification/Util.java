package br.com.allin.mobile.pushnotification;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.allin.mobile.pushnotification.constants.Preferences;
import br.com.allin.mobile.pushnotification.entity.DeviceEntity;

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
    public static boolean isNullOrClear(String value) {
        return value == null || TextUtils.isEmpty(value.trim());
    }

    /**
     * Verifies that the device has Google Play Services (APK) installed.
     *
     * @param context Application context.
     *
     * @return If the device has the apk Google Play Services installed returns {@code true}.
     * Otherwise, returns {@code false}.
     */
    public static boolean checkPlayServices(Context context) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);

        return resultCode == ConnectionResult.SUCCESS;
    }

    /**
     * Return the application version. The version is set in the key
     * {@code android:versionCode} of {@code AndroidManifest.xml}.
     *
     * @param context Application context.
     *
     * @return Application version.
     */
    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Verifies that the device is connected to any network (WiFi or Mobile).
     *
     * <b>Attention:</b> This method only checks if the device is connected,
     * there is no connectivity (eg does not check for
     * available WiFi networks or if there is 3G signal).
     *
     * @param context Application context.
     *
     * @return If the device is connected returns {@code true}. Otherwise, returns {@code false}.
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
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
                .getIdentifier("all_in_token", "string", context.getPackageName());

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
        final String MD5 = "MD5";
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(value.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);

                while (h.length() < 2) {
                    h = "0" + h;
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
