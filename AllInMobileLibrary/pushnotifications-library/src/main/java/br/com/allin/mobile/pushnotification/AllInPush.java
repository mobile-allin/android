package br.com.allin.mobile.pushnotification;

import android.content.Context;

import java.util.Map;

import br.com.allin.mobile.pushnotification.entity.ConfigurationEntity;
import br.com.allin.mobile.pushnotification.entity.DeviceEntity;
import br.com.allin.mobile.pushnotification.exception.NotNullAttributeOrPropertyException;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;
import br.com.allin.mobile.pushnotification.service.CampaignService;
import br.com.allin.mobile.pushnotification.service.ConfigurationService;
import br.com.allin.mobile.pushnotification.service.DeviceService;
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
public class AllInPush {
    private AllInPush() {
    }

    private AllInApplication alliNApplication;

    public void setAlliNApplication(AllInApplication alliNApplication) {
        this.alliNApplication = alliNApplication;
    }

    public AllInApplication getAlliNApplication() {
        return alliNApplication;
    }

    public Context getContext() {
        return alliNApplication;
    }

    private static AllInPush allInPush;

    public static AllInPush getInstance() {
        if (allInPush == null) {
            allInPush = new AllInPush();
        }

        return allInPush;
    }

    /**
     * <b>Asynchronous</b> - Configure the application by sending to the default list,
     * starting GCM (Google Cloud Message) and checking the ID of AllIn
     *
     * @param allInApplication Application (Context)
     * @param configurationEntity Settings such as SenderID and TokenAllIn
     *
     * @throws NotNullAttributeOrPropertyException Parameter
     * application or configurationEntity is null
     */
    public void configure(AllInApplication allInApplication,
                          ConfigurationEntity configurationEntity)
            throws NotNullAttributeOrPropertyException {
        this.configure(allInApplication, configurationEntity, null);
    }

    /**
     * <b>Asynchronous</b> - Configure the application by sending to the default list,
     * starting GCM (Google Cloud Message) and checking the ID of AllIn
     *
     * @param allInApplication Application (Context)
     * @param configurationEntity Settings such as SenderID and TokenAllIn
     * @param onRequest Interface that returns success or error in the request
     *
     * @throws NotNullAttributeOrPropertyException Parameter
     * application or configurationEntity is null
     */
    public void configure(final AllInApplication allInApplication,
                          final ConfigurationEntity configurationEntity,
                          final OnRequest onRequest)
            throws NotNullAttributeOrPropertyException {

        this.setAlliNApplication(allInApplication);

        new ConfigurationService(allInApplication, configurationEntity, onRequest).init();
    }

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
     * <b>Asynchronous</b> - Updates the e-mail in the database
     *
     * @param userEmail E-mail that is registered in the database of AllIn
     * @param onRequest Interface that returns success or error in the request
     */
    public void updateUserEmail(final String userEmail, final OnRequest onRequest) {
        new DeviceService(getContext(), onRequest).updateEmail(userEmail);
    }

    public void sendDeviceInfo(final DeviceEntity deviceEntity) {
        this.sendDeviceInfo(deviceEntity, null);
    }

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

    public void logout() {
        this.logout(null);
    }

    public void logout(final OnRequest onRequest) {
        new DeviceService(getContext(), onRequest).logout();
    }

    /**
     * <b>Asynchronous</b> - Register push the event (According to the enum Action)
     *
     * @param idCampaign Action push notification (according to options in the Action Enum)
     */
    public void notificationCampaign(final int idCampaign) {
        this.notificationCampaign(idCampaign, null);
    }

    /**
     * <b>Asynchronous</b> - Register push the event (According to the enum Action)
     *
     * @param idCampaign Action push notification (according to options in the Action Enum)
     * @param onRequest Interface that returns success or error in the request
     */
    public void notificationCampaign(final int idCampaign, final OnRequest onRequest) {
        new NotificationService().sendCampaign(idCampaign, getContext(), onRequest);
    }

    public void notificationTransactional(final int idSend) {
        this.notificationTransactional(idSend, null);
    }

    public void notificationTransactional(final int idSend, final OnRequest onRequest) {
        new NotificationService().sendTransactional(idSend, getContext(), onRequest);
    }

    /**
     * @return E-mail the saved user in SharedPreferences
     */
    public String getUserEmail() {
        return new DeviceService(getContext()).getUserEmail();
    }

    /**
     * @return Device identification on Google
     */
    public String getDeviceId() {
        return new DeviceService(getContext()).getDeviceToken();
    }
}
