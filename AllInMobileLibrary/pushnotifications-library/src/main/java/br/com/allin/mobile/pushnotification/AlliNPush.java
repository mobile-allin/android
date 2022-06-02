package br.com.allin.mobile.pushnotification;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.lang.ref.WeakReference;
import java.util.List;

import br.com.allin.mobile.pushnotification.dao.AlliNDatabase;
import br.com.allin.mobile.pushnotification.entity.allin.AINotification;
import br.com.allin.mobile.pushnotification.entity.allin.AIValues;
import br.com.allin.mobile.pushnotification.notification.Notification;
import br.com.allin.mobile.pushnotification.service.allin.CacheService;
import br.com.allin.mobile.pushnotification.service.allin.DeviceService;
import br.com.allin.mobile.pushnotification.service.allin.NotificationService;

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

    public void registerForPushNotifications(@NonNull final Context context,
                                             @Nullable final String tokenToUpdate) {
        this.setContext(context);

        FirebaseApp.initializeApp(context);

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            String newToken = task.getResult();

            if (newToken != null) {
                String oldToken = TextUtils.isEmpty(tokenToUpdate) ? AlliNPush.getInstance(getContext()).getDeviceToken() : tokenToUpdate;

                if (TextUtils.isEmpty(oldToken) || !newToken.equals(oldToken)) {
                    DeviceService deviceService = new DeviceService();
                    deviceService.sendDevice(oldToken, newToken);
                }
            }
        });

        new CacheService().sync();
    }

    public Context getContext() {
        if (this.contextWeakReference != null && this.contextWeakReference.get() != null) {
            return this.contextWeakReference.get();
        } else {
            try {
                return this.getApplication().getApplicationContext();
            } catch (Exception e) {
                return null;
            }
        }
    }

    public void setContext(Context context) {
        this.contextWeakReference = new WeakReference<>(context);

        AlliNDatabase.initialize(context);
    }

    @SuppressLint({"PrivateApi"})
    private Application getApplication() throws Exception {
        return (Application)Class.forName("android.app.AppGlobals")
                .getMethod("getInitialApplication").invoke(null, (Object[])null);
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
     * @return Device identification on Google saved in SharedPreferences
     */
    public String getDeviceToken() {
        return new DeviceService().getDeviceToken();
    }

    /**
     * <b>Asynchronous</b> - This method removes the link between the email and the device
     */
    public void logout(String email) {
        new DeviceService().logout(email);
    }

    public List<AINotification> getNotifications() {
        return NotificationService.getList();
    }

    public static void showNotification(Context context, RemoteMessage remoteMessage) {
        Notification notification = new Notification(context);
        notification.showNotification(remoteMessage);
    }

    public static void updateDeviceToken(Context context, String newToken) {
        DeviceService deviceService = new DeviceService();
        deviceService.sendDevice(AlliNPush.getInstance(context).getDeviceToken(), newToken);
    }
}