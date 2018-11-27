package br.com.allin.mobile.pushnotification;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.lang.ref.WeakReference;
import java.util.List;

import br.com.allin.mobile.pushnotification.configuration.AlliNConfiguration;
import br.com.allin.mobile.pushnotification.dao.AlliNDatabase;
import br.com.allin.mobile.pushnotification.entity.allin.AIMessage;
import br.com.allin.mobile.pushnotification.entity.allin.AIValues;
import br.com.allin.mobile.pushnotification.interfaces.AllInDelegate;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;
import br.com.allin.mobile.pushnotification.service.allin.CacheService;
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
 * <p>
 * <br><br>
 * <b>Required Permissions:</b>
 * <p>
 * <br><br>
 * <u><b>Permission to receive push notification:</b></u>
 * <br><br>
 * <p>
 * {@code <uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />}
 * {@code <uses-permission android:name="android.permission.WAKE_LOCK" />}
 * <p>
 * <br><br>
 * <u><b>Permission to access the internet:</b></u>
 * <br><br>
 * <p>
 * {@code <uses-permission android:name="android.permission.INTERNET" />}
 * <p>
 * <br><br>
 * <u><b>Permission to view status of the internet (online or offline):</b></u>
 * <br><br>
 * <p>
 * {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}
 * <p>
 * <br><br>
 * <u><b>Geolocation permissions:</b></u>
 * <br><br>
 * <p>
 * {@code <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />}
 * <br>
 * {@code <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />}
 * <p>
 * <br><br><br>
 * You must also <b>add to Manifest</b> the following lines:
 * <br><br>
 * <u><b>To allow the opening of our WebView:</b></u>
 * <br><br>
 * {@code
 * <activity
 * android:name="br.com.allin.mobile.pushnotification.webview.AllInWebViewActivity"
 * android:theme="@style/Theme.AppCompat.Light.NoActionBar" />
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
 * compile 'com.google.android.gms:play-services-location:15.0.1'
 * <br>
 * compile 'com.android.support:appcompat-v7:23.4.0'
 * <br>
 * <br>
 * <br>
 * <b>OBS: These settings are required for the proper functioning of lib.</b>
 */
public class AlliNPush {
    private static AlliNPush alliNPush;

    private AlliNPush() {
    }

    /**
     * Singleton instance of class
     */
    public static AlliNPush getInstance() {
        return AlliNPush.getInstance(null);
    }

    public static AlliNPush getInstance(Context context) {
        if (AlliNPush.alliNPush == null) {
            AlliNPush.alliNPush = new AlliNPush();
        }

        if (context != null) {
            AlliNPush.alliNPush.setContext(context);
        }

        return AlliNPush.alliNPush;
    }

    private WeakReference<Context> contextWeakReference;

    public void registerForPushNotifications(@NonNull Context context) {
        this.registerForPushNotifications(context, null);
    }

    public void registerForPushNotifications(@NonNull final Context context, AllInDelegate delegate) {
        this.setContext(context);

        FirebaseApp.initializeApp(context);
        AlliNConfiguration.getInstance().initialize(delegate);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String oldToken = AlliNPush.getInstance(context).getDeviceToken();
                String newToken = instanceIdResult.getToken();

                if (TextUtils.isEmpty(oldToken) || !newToken.equals(oldToken)) {
                    DeviceService deviceService = new DeviceService();
                    deviceService.sendDevice(oldToken, newToken);
                }
            }
        });

        new CacheService().sync();
    }

    public Context getContext() {
        return this.contextWeakReference.get();
    }

    public void setContext(Context context) {
        this.contextWeakReference = new WeakReference<>(context);

        AlliNDatabase.initialize(context);
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
    public void registerEmail(String userEmail) {
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
     * @param nmList           Mailing list that will be sent
     * @param columnsAndValues Map with key and value for formation of the JSON API
     */
    public void sendList(String nmList, List<AIValues> columnsAndValues) {
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

    public void setDeviceToken(String deviceToken) {
        new DeviceService().setDeviceToken(deviceToken);
    }

    /**
     * @return History push's received in application
     */
    public List<AIMessage> getMessages() {
        return new MessageService().getMessages();
    }

    /**
     * This method is used to remove a history message
     *
     * @param message The AIMessage object is created automatically by the framework
     */
    public long addMessage(AIMessage message) {
        return new MessageService().addMessage(message);
    }

    /**
     * This method is used to remove a history message
     *
     * @param id Identification of push received in application
     */
    public void deleteMessage(int id) {
        new MessageService().deleteMessage(id);
    }

    /**
     * @param id Identification of push received in application
     */
    public void messageHasBeenRead(long id) {
        new MessageService().hasBeenRead(id);
    }
}