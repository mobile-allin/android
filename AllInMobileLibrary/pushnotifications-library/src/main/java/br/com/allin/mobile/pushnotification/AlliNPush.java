package br.com.allin.mobile.pushnotification;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import br.com.allin.mobile.pushnotification.configuration.AlliNConfiguration;
import br.com.allin.mobile.pushnotification.entity.allin.BaseEntity;
import br.com.allin.mobile.pushnotification.entity.allin.ConfigurationEntity;
import br.com.allin.mobile.pushnotification.entity.allin.ContextEntity;
import br.com.allin.mobile.pushnotification.entity.allin.MessageEntity;
import br.com.allin.mobile.pushnotification.entity.allin.NotificationEntity;
import br.com.allin.mobile.pushnotification.helper.FieldHelper;
import br.com.allin.mobile.pushnotification.interfaces.AllInDelegate;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;
import br.com.allin.mobile.pushnotification.service.allin.ConfigurationService;
import br.com.allin.mobile.pushnotification.service.allin.DeviceService;
import br.com.allin.mobile.pushnotification.service.allin.MessageService;
import br.com.allin.mobile.pushnotification.service.allin.StatusService;

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
public class AlliNPush {
    private static String SENDER_ID = "allin.senderid";
    private static String APP_ID = "allin.token";
    private static String WHITE_ICON = "allin_notification_white_icon";
    private static String ICON = "allin_notification_icon";
    private static String BACKGROUND = "allin_notification_background";

    private AlliNPush() {
    }

    private static AlliNPush alliNPush;

    /**
     * Singleton instance of class
     */
    public static AlliNPush getInstance() {
        if (alliNPush == null) {
            alliNPush = new AlliNPush();
        }

        return alliNPush;
    }

    private ContextEntity context;

    public void registerForPushNotifications(@NonNull Context context) {
        this.registerForPushNotifications(context, null);
    }

    public void registerForPushNotifications(@NonNull Context context, AllInDelegate allInDelegate) {
        this.context = new ContextEntity(context);

        try {
            ApplicationInfo applicationInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);

            if (applicationInfo != null) {
                @DrawableRes
                int whiteIcon = FieldHelper.getResId(AlliNPush.WHITE_ICON, "drawable");
                @DrawableRes
                int icon = FieldHelper.getResId(AlliNPush.ICON, "drawable");
                @ColorRes
                int background = FieldHelper.getResId(AlliNPush.BACKGROUND, "color");

                String senderId = applicationInfo.metaData.getString(AlliNPush.SENDER_ID);
                String appId = applicationInfo.metaData.getString(AlliNPush.APP_ID);

                if (senderId == null || TextUtils.isEmpty(senderId.trim())) {
                    Log.d("AlliN Push", "Required meta-data 'allin.senderid' in MANIFEST");
                } else if (appId == null || TextUtils.isEmpty(appId.trim())) {
                    Log.d("AlliN Push", "Required meta-data 'allin.appid' in MANIFEST");
                }

                AlliNConfiguration.getInstance().init(allInDelegate);

                NotificationEntity notification = new NotificationEntity(background, icon, whiteIcon);
                ConfigurationEntity configuration = new ConfigurationEntity(senderId, notification);

                new ConfigurationService(configuration).init();
            }
        } catch (PackageManager.NameNotFoundException nameNotFoundException) {
            nameNotFoundException.printStackTrace();
        }
    }

    public AllInDelegate getAllInDelegate() {
        return AlliNConfiguration.getInstance().getAllInDelegate();
    }

    public Context getContext() {
        return context.getApplicationContext();
    }

    public void finish() {
        AlliNConfiguration.getInstance().finish();
    }

    /**
     * <b>Asynchronous</b> - Disable notifications on the server
     */
    public void disable() {
        new StatusService().disable();
    }

    /**
     * <b>Asynchronous</b> - Enable notifications on the server
     */
    public void enable() {
        new StatusService().enable();
    }

    /**
     * <b>Asynchronous</b> - Checks whether notifications are enabled on the server
     *
     * @param onRequest Interface that returns success or error in the request
     */
    public void deviceIsEnable(OnRequest onRequest) {
        new StatusService(onRequest).deviceIsEnable();
    }

    /**
     * <b>Asynchronous</b> - Updates the e-mail in the database and save in SharedPreferences
     *
     * @param userEmail E-mail that is registered in the database of AllIn
     */
    public void registerEmail(final String userEmail) {
        new DeviceService().registerEmail(userEmail);
    }

    /**
     * @return E-mail saved in SharedPreferences
     */
    public String getEmail() {
        return new DeviceService().getEmail();
    }

    public String getIdentifier() {
        return new DeviceService().getIdentifier();
    }

    /**
     * <b>Asynchronous</b> - Shipping to list
     *
     * @param nmList Mailing list that will be sent
     * @param columnsAndValues Map with key and value for formation of the JSON API
     */
    public void sendList(String nmList, List<BaseEntity> columnsAndValues) {
        new DeviceService().sendList(nmList, columnsAndValues);
    }

    /**
     * <b>Asynchronous</b> - This method removes the link between the email and the device
     */
    public void logout() {
        new DeviceService().logout();
    }

    /**
     * @return Device identification on Google saved in SharedPreferences
     */
    public String getDeviceToken() {
        return new DeviceService().getDeviceToken();
    }

    /**
     * @return History push's received in application
     */
    public List<MessageEntity> getMessages(Context context) {
        return new MessageService(context).getMessages();
    }

    /**
     * This method is used to remove a history message
     *
     * @param messageEntity The MessageEntity object is created automatically by the framework
     *
     * @return Identification of push received in application
     */
    public long addMessage(Context context, MessageEntity messageEntity) {
        return new MessageService(context).addMessage(messageEntity);
    }

    /**
     * This method is used to remove a history message
     *
     * @param id Identification of push received in application
     *
     * @return If successfully deleted
     */
    public boolean deleteMessage(Context context, int id) {
        return new MessageService(context).deleteMessage(id);
    }

    /**
     * @param id Identification of push received in application
     *
     * @return If successfully updated
     */
    public boolean messageHasBeenRead(Context context, int id) {
        return new MessageService(context).messageHasBeenRead(id);
    }
}