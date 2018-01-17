package br.com.allin.mobile.pushnotification;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.List;
import java.util.Map;

import br.com.allin.mobile.pushnotification.configurations.AlliNConfiguration;
import br.com.allin.mobile.pushnotification.entity.ConfigurationEntity;
import br.com.allin.mobile.pushnotification.entity.ContextEntity;
import br.com.allin.mobile.pushnotification.entity.DeviceEntity;
import br.com.allin.mobile.pushnotification.entity.MessageEntity;
import br.com.allin.mobile.pushnotification.entity.NotificationEntity;
import br.com.allin.mobile.pushnotification.exception.NotNullAttributeOrPropertyException;
import br.com.allin.mobile.pushnotification.helper.FieldHelper;
import br.com.allin.mobile.pushnotification.interfaces.AllInDelegate;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;
import br.com.allin.mobile.pushnotification.service.CampaignService;
import br.com.allin.mobile.pushnotification.service.ConfigurationService;
import br.com.allin.mobile.pushnotification.service.DeviceService;
import br.com.allin.mobile.pushnotification.service.MessageService;
import br.com.allin.mobile.pushnotification.service.NotificationService;
import br.com.allin.mobile.pushnotification.service.StatusService;

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

    private ContextEntity context;

    private AlliNPush() {
    }

    private static AlliNPush alliNPush;

    /**
     * Instance of class
     */
    public static AlliNPush getInstance() {
        if (alliNPush == null) {
            alliNPush = new AlliNPush();
        }

        return alliNPush;
    }

    private AllInDelegate allInDelegate;

    public void registerForPushNotifications(@NonNull Context context,
                                             @NonNull AllInDelegate allInDelegate) throws Exception {
        this.allInDelegate = allInDelegate;
        this.context = new ContextEntity(context);

        try {
            ApplicationInfo applicationInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);

            if (applicationInfo != null) {
                @DrawableRes
                int whiteIcon = FieldHelper.getResId(AlliNPush.WHITE_ICON, Drawable.class);
                @DrawableRes
                int icon = FieldHelper.getResId(AlliNPush.ICON, Drawable.class);
                @ColorRes
                int background = FieldHelper.getResId(AlliNPush.BACKGROUND, Color.class);

                String senderId = applicationInfo.metaData.getString(AlliNPush.SENDER_ID);
                String appId = applicationInfo.metaData.getString(AlliNPush.APP_ID);

                if (senderId == null || TextUtils.isEmpty(senderId.trim())) {
                    throw new Exception("Required meta-data 'allin.senderid' in MANIFEST");
                } else if (appId == null || TextUtils.isEmpty(appId.trim())) {
                    throw new Exception("Required meta-data 'allin.appid' in MANIFEST");
                }

                AlliNConfiguration.getInstance().init(context, allInDelegate);

                NotificationEntity notification = new NotificationEntity(background, icon, whiteIcon);
                ConfigurationEntity configuration = new ConfigurationEntity(senderId, notification);

                new ConfigurationService(configuration).init();
            }
        } catch (PackageManager.NameNotFoundException nameNotFoundException) {
            nameNotFoundException.printStackTrace();
        }
    }

    public AllInDelegate getAllInDelegate() {
        return allInDelegate;
    }

    public Context getContext() {
        return context.getApplicationContext();
    }

    /*
    public static void finish() {
        AlliNConfiguration.getInstance().finish();
    } */

    /**
     * <b>Asynchronous</b> - Disable notifications on the server
     */
    public void disable() {
        new StatusService(getContext(), null).disable();
    }

    /**
     * <b>Asynchronous</b> - Disable notifications on the server
     *
     * @param onRequest Interface that returns success or error in the request
     */
    public void disable(final OnRequest onRequest) {
        new StatusService(getContext(), onRequest).disable();
    }

    /**
     * <b>Asynchronous</b> - Enable notifications on the server
     */
    public void enable() {
        new StatusService(getContext(), null).enable();
    }

    /**
     * <b>Asynchronous</b> - Enable notifications on the server
     *
     * @param onRequest Interface that returns success or error in the request
     */
    public void enable(final OnRequest onRequest) {
        new StatusService(getContext(), onRequest).enable();
    }

    /**
     * <b>Asynchronous</b> - Checks whether notifications are enabled on the server
     *
     * @param onRequest Interface that returns success or error in the request
     */
    public void deviceIsEnable(final OnRequest onRequest) {
        new StatusService(getContext(), onRequest).deviceIsEnable();
    }

    /**
     * <b>Asynchronous</b> - Returns the HTML campaign created
     *
     * @param idCampaign Template ID that the push notification returns
     * @param onRequest Interface that returns success or error in the request
     */
    public void getHtmlTemplate(final int idCampaign, final OnRequest onRequest) {
        new CampaignService(getContext(), onRequest).getTemplate(idCampaign);
    }

    /**
     * <b>Asynchronous</b> - Updates the e-mail in the database
     *
     * @param userEmail E-mail that is registered in the database of AllIn
     */
    public void updateUserEmail(final String userEmail) {
        this.updateUserEmail(userEmail, null);
    }

    /**
     * <b>Asynchronous</b> - Updates the e-mail in the database and save in SharedPreferences
     *
     * @param userEmail E-mail that is registered in the database of AllIn
     * @param onRequest Interface that returns success or error in the request
     */
    public void updateUserEmail(final String userEmail, final OnRequest onRequest) {
        new DeviceService(getContext(), onRequest).updateEmail(userEmail);
    }

    /**
     * <b>Asynchronous</b> - Sends the device information to the server
     *
     * @param deviceEntity Object with the device information
     */
    public void sendDeviceInfo(final DeviceEntity deviceEntity) {
        this.sendDeviceInfo(deviceEntity, null);
    }

    /**
     * <b>Asynchronous</b> - Sends the device information to the server
     *
     * @param deviceEntity Object with the device information
     * @param onRequest Interface that returns success or error in the request
     */
    public void sendDeviceInfo(final DeviceEntity deviceEntity, final OnRequest onRequest) {
        new DeviceService(getContext(), onRequest).sendDevice(deviceEntity);
    }

    /**
     * <b>Asynchronous</b> - Shipping to list
     *
     * @param nmList Mailing list that will be sent
     * @param values Map with key and value for formation of the JSON API
     */
    public void sendList(final String nmList, final Map<String, String> values) {
        this.sendList(nmList, values, null);
    }

    /**
     * <b>Asynchronous</b> - Shipping to list
     *
     * @param nmList Mailing list that will be sent
     * @param values Map with key and value for formation of the JSON API
     * @param onRequest Interface that returns success or error in the request
     */
    public void sendList(final String nmList,
                         final Map<String, String> values, final OnRequest onRequest) {
        new DeviceService(getContext(), onRequest).sendList(nmList, values);
    }

    /**
     * <b>Asynchronous</b> - This method removes the link between the email and the device
     */
    public void logout() {
        this.logout(null);
    }

    /**
     * <b>Asynchronous</b> - This method removes the link between the email and the device
     *
     * @param onRequest Interface that returns success or error in the request
     */
    public void logout(final OnRequest onRequest) {
        new DeviceService(getContext(), onRequest).logout();
    }

    /**
     * <b>Asynchronous</b> - Saves click the campaign push
     * (the method does not possess callback because it is used only to register the click)
     *
     * @param idCampaign Campaign identification received from server
     * @param date Date of the campaign received from server
     */
    public void notificationCampaign(final int idCampaign, final String date) {
        new NotificationService().sendCampaign(idCampaign, date, getContext());
    }

    /**
     * <b>Asynchronous</b> - Saves click the transactional send push
     * (the method does not possess callback because it is used only to register the click)
     *
     * @param idSend Sending identification received from server
     * @param date Date of the campaign received from server
     */
    public void notificationTransactional(final int idSend, final String date) {
        new NotificationService().sendTransactional(idSend, date, getContext());
    }

    /**
     * @return E-mail saved in SharedPreferences
     */
    public String getUserEmail() {
        return new DeviceService(getContext()).getUserEmail();
    }

    /**
     * @return Device identification on Google saved in SharedPreferences
     */
    public String getDeviceId() {
        return new DeviceService(getContext()).getDeviceToken();
    }

    /**
     * @return History push's received in application
     */
    public List<MessageEntity> getMessages() {
        return new MessageService(getContext()).getMessages();
    }

    /**
     * This method is used to remove a history message
     *
     * @param messageEntity The MessageEntity object is created automatically by the framework
     *
     * @return Identification of push received in application
     */
    public long addMessage(MessageEntity messageEntity) {
        return new MessageService(getContext()).addMessage(messageEntity);
    }

    /**
     * This method is used to remove a history message
     *
     * @param id Identification of push received in application
     *
     * @return If successfully deleted
     */
    public boolean deleteMessage(int id) {
        return new MessageService(getContext()).deleteMessage(id);
    }

    /**
     * @param id Identification of push received in application
     *
     * @return If successfully updated
     */
    public boolean messageHasBeenRead(int id) {
        return new MessageService(getContext()).messageHasBeenRead(id);
    }
}
