package br.com.allin.mobile.pushnotification;

import android.app.Application;
import android.content.Context;

import java.util.Map;

import br.com.allin.mobile.pushnotification.exception.GenerateDeviceIdException;
import br.com.allin.mobile.pushnotification.exception.NotNullAttributeOrPropertyException;
import br.com.allin.mobile.pushnotification.listener.ConfigurationListener;
import br.com.allin.mobile.pushnotification.model.ConfigurationOptions;

/**
 * @author lucasrodrigues
 * <br>
 * <br>
 * <b>MINIMUN VERSION OF ANDROID: 4.0 (14)</b>
 * <br>
 * <br>
 * <b>To add value to the strings file with the name "all_in_token"</b>
 * and own lib will try to get this information
 *
 * <br><br>
 * <b>Required Permissions:</b>
 *
 * <br><br>
 * <u><b>Permission to receive push notification:</b></u>
 * <br><br>
 *
 * {@code <uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />}
 * {@code <uses-permission android:name="android.permission.WAKE_LOCK" />}
 *
 * <br><br>
 * <u><b>Permission to access the internet:</b></u>
 * <br><br>
 *
 * {@code <uses-permission android:name="android.permission.INTERNET" />}
 *
 * <br><br>
 * <u><b>Permission to view status of the internet (online or offline):</b></u>
 * <br><br>
 *
 * {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}
 *
 * <br><br>
 * <u><b>Geolocation permissions:</b></u>
 * <br><br>
 *
 * {@code <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />}
 * <br>
 * {@code <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />}
 *
 * <br><br><br>
 * You must also <b>add to Manifest</b> the following lines:
 * <br><br>
 * <u><b>To allow the opening of our WebView:</b></u>
 * <br><br>
 * {@code
 * <activity
 *     android:name="br.com.allin.mobile.pushnotification.webview.AllInWebViewActivity"
 *     android:theme="@style/Theme.AppCompat.Light.NoActionBar" />
 * }
 * <br><br>
 * <u><b>To configure the GCM:</b></u>
 * <br>
 * <br>
 * <pre>{@code <service
 *     android:name="br.com.allin.mobile.pushnotification.gcm.AllInGcmListenerService"
 *     android:exported="false">
 *         <intent-filter>
 *             <action android:name="com.google.android.c2dm.intent.RECEIVE" />
 *         </intent-filter>
 * </service>
 *
 * <receiver
 *     android:name="com.google.android.gms.gcm.GcmReceiver"
 *     android:exported="true"
 *     android:permission="com.google.android.c2dm.permission.SEND">
 *         <intent-filter>
 *             <action android:name="com.google.android.c2dm.intent.RECEIVE" />
 *             <action android:name="com.google.android.c2dm.intent.REGISTRATION" />
 *             <category android:name="br.com.allin.mobile.pushnotification" />
 *         </intent-filter>
 * </receiver>} </pre>
 * <u><b>You need to add to your gradle file the following dependencies:</b></u>
 * <br>
 * <br>
 * compile 'com.google.android.gms:play-services-gcm:7.8.0'
 * <br>
 * compile 'com.google.android.gms:play-services-location:7.8.0'
 * <br>
 * compile 'com.android.support:appcompat-v7:23.4.0'
 * <br>
 * <br>
 * <br>
 * <b>OBS: These settings are required for the proper functioning of lib.</b>
 */
public class AllInPush {
    /**
     * Enum for registration on the server (click or show)
     */
    public enum Action {
        CLICK,
        SHOW
    }

    private AllInPush() {
    }

    /**
     * <b>Asynchronous</b> - Configure the application by sending to the default list,
     * starting GCM (Google Cloud Message) and checking the ID of AllIn
     *
     * @param application Application (Context)
     * @param configurationOptions Settings such as SenderID and TokenAllIn
     *
     * @throws NotNullAttributeOrPropertyException Parameter application or configurationOptions is null
     * @throws GenerateDeviceIdException Problems Generating Device ID on Google
     */
    public static void configure(Application application, ConfigurationOptions configurationOptions)
            throws NotNullAttributeOrPropertyException, GenerateDeviceIdException {
        configure(application, configurationOptions, null);
    }

    /**
     * <b>Asynchronous</b> - Configure the application by sending to the default list,
     * starting GCM (Google Cloud Message) and checking the ID of AllIn
     *
     * @param application Application (Context)
     * @param configurationOptions Settings such as SenderID and TokenAllIn
     * @param configurationListener Interface that returns success or error in the request
     *
     * @throws NotNullAttributeOrPropertyException Parameter application or configurationOptions is null
     * @throws GenerateDeviceIdException Problems Generating Device ID on Google
     */
    public static void configure(Application application, ConfigurationOptions configurationOptions,
                                 ConfigurationListener configurationListener)
            throws NotNullAttributeOrPropertyException, GenerateDeviceIdException {
        Manager.getInstance().configure(application, configurationOptions, configurationListener);
    }

    /**
     * <b>Asynchronous</b> - Disable notifications on the server
     *
     * @param configurationListener Interface that returns success or error in the request
     */
    public static void disable(ConfigurationListener configurationListener) {
        Manager.getInstance().disable(configurationListener);
    }

    /**
     * <b>Asynchronous</b> - Enable notifications on the server
     *
     * @param configurationListener Interface that returns success or error in the request
     */
    public static void enable(ConfigurationListener configurationListener) {
        Manager.getInstance().enable(configurationListener);
    }

    public static void logout(ConfigurationListener configurationListener) {
        Manager.getInstance().logout(configurationListener);
    }

    /**
     * <b>Asynchronous</b> - Checks whether notifications are enabled on the server
     *
     * @param configurationListener Interface that returns success or error in the request
     */
    public static void deviceIsEnable(ConfigurationListener configurationListener) {
        Manager.getInstance().deviceIsEnable(configurationListener);
    }

    /**
     * <b>Asynchronous</b> - Returns the HTML campaign created
     *
     * @param id Template ID that the push notification returns
     * @param configurationListener Interface that returns success or error in the request
     */
    public static void getHtmlTemplate(int id, ConfigurationListener configurationListener) {
        Manager.getInstance().getHtmlTemplate(id, configurationListener);
    }

    /**
     * <b>Asynchronous</b> - Updates the e-mail in the database
     *
     * @param userEmail E-mail that is registered in the database of AllIn
     * @param configurationListener Interface that returns success or error in the request
     */
    public static void updateUserEmail(String userEmail,
                                       ConfigurationListener configurationListener) {
        if (!Manager.getInstance().isConfigurationLoaded()) {
            if (configurationListener != null) {
                configurationListener.onError(
                        new Exception("Necessário configurar a sdk. AllInPush.configure()"));
            }

            return;
        }

        Manager.getInstance().updateUserEmail(userEmail, configurationListener);
    }

    /**
     * <b>Asynchronous</b> - Shipping to list
     *
     * @param nmList Mailing list that will be sent
     * @param values Map with key and value for formation of the JSON API
     * @param configurationListener Interface that returns success or error in the request
     */
    public static void sendList(String nmList, Map<String, String> values,
                                ConfigurationListener configurationListener) {
        if (!Manager.getInstance().isConfigurationLoaded()) {
            if (configurationListener != null) {
                configurationListener.onError(
                        new Exception("Necessário configurar a sdk. AllInPush.configure()"));
            }

            return;
        }

        StringBuilder campos = new StringBuilder();
        StringBuilder valor = new StringBuilder();

        for (String key : values.keySet()) {
            campos.append(key).append(";");

            String value = values.get(key);

            if (value != null && value.trim().length() > 0) {
                valor.append(value);
            }

            valor.append(";");
        }

        Manager.getInstance().sendList(nmList,
                campos.toString(), valor.toString(), configurationListener);
    }


    /**
     * <b>Asynchronous</b> - Register push the event (According to the enum Action)
     *
     * @param action Action push notification (according to options in the Action Enum)
     * @param configurationListener Interface that returns success or error in the request
     */
    public static void registerNotificationAction(Action action,
                                                  ConfigurationListener configurationListener) {
        Manager.getInstance().registerNotificationAction(action, configurationListener);
    }

    /**
     * @param context Application context
     * @return E-mail the saved user in SharedPreferences
     */
    public static String getUserEmail(Context context) {
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(context);
        String userEmail = sharedPreferencesManager
                .getData(SharedPreferencesManager.KEY_USER_EMAIL, null);

        return userEmail != null && userEmail.trim().length() > 0 ? userEmail : null;
    }

    /**
     * @param context Application context
     * @return Device identification on Google
     */
    public static String getDeviceId(Context context) {
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(context);
        String deviceId = sharedPreferencesManager
                .getData(SharedPreferencesManager.KEY_DEVICE_ID, null);

        return deviceId != null && deviceId.trim().length() > 0 ? deviceId : null;
    }
}
