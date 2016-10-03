package br.com.allin.mobile.pushnotification;

import android.app.Application;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.com.allin.mobile.pushnotification.constants.HttpBody;
import br.com.allin.mobile.pushnotification.constants.Parameters;
import br.com.allin.mobile.pushnotification.constants.Preferences;
import br.com.allin.mobile.pushnotification.constants.Route;
import br.com.allin.mobile.pushnotification.dao.CacheDAO;
import br.com.allin.mobile.pushnotification.entity.ConfigurationOptions;
import br.com.allin.mobile.pushnotification.entity.DeviceInfos;
import br.com.allin.mobile.pushnotification.entity.NotificationSettings;
import br.com.allin.mobile.pushnotification.entity.ResponseData;
import br.com.allin.mobile.pushnotification.exception.GenerateDeviceIdException;
import br.com.allin.mobile.pushnotification.exception.NetworkException;
import br.com.allin.mobile.pushnotification.exception.NotNullAttributeOrPropertyException;
import br.com.allin.mobile.pushnotification.exception.WebServiceException;
import br.com.allin.mobile.pushnotification.gcm.AllInLocation;
import br.com.allin.mobile.pushnotification.http.HttpManager;
import br.com.allin.mobile.pushnotification.interfaces.ConfigurationListener;
import br.com.allin.mobile.pushnotification.interfaces.OnAllInLocationChange;

/**
 * Class used to intermediate requests to the server
 */
public class Manager {
    private static Manager manager;
    private AllInApplication allInApplication = null;
    private ConfigurationOptions configOptions = null;
    private SharedPreferencesManager prefManager = null;

    private Manager() {
    }

    /**
     * Initialize the class
     *
     * @return Instance of the class
     */
    public static Manager getInstance() {
        if (manager == null) {
            manager = new Manager();
        }

        return manager;
    }

    /**
     * <b>Asynchronous</b> - Configure the application by sending to the default list,
     * starting GCM (Google Cloud Message) and checking the ID of AllIn
     *
     * @param allInApplication      Application (Context)
     * @param configurationOptions  Settings such as SenderID and TokenAllIn
     * @param configurationListener Interface that returns success or error in the request
     * @throws NotNullAttributeOrPropertyException Parameter application or configurationOptions is null
     * @throws GenerateDeviceIdException           Problems Generating Device ID on Google
     */
    public void configure(final AllInApplication allInApplication,
                          final ConfigurationOptions configurationOptions,
                          final ConfigurationListener configurationListener)
            throws NotNullAttributeOrPropertyException, GenerateDeviceIdException {

        validateParams(allInApplication, configurationOptions);

        this.allInApplication = allInApplication;
        this.configOptions = configurationOptions;
        this.prefManager = new SharedPreferencesManager(allInApplication);

        CacheDAO.getInstance(allInApplication).sync();

        configureNotifications(configurationOptions.getNotificationSettings());

        DeviceInfos deviceInfos = getDeviceId();

        if (deviceInfos == null ||
                TextUtils.isEmpty(deviceInfos.getDeviceId()) || deviceInfos.isRenewId()) {
            generateDeviceToken(deviceInfos, configurationListener);
        } else {
            sendDeviceInfo(deviceInfos, configurationListener);
        }
    }

    private DeviceInfos getDeviceId() {
        String deviceId = prefManager.getData(Preferences.DEVICE_ID, null);
        Integer registeredVersion = prefManager.getData(Preferences.APPVERSION, 1);
        String sharedProjectId = prefManager.getData(Preferences.PROJECT_ID, null);

        if (Util.isNullOrClear(deviceId)) {
            return null;
        }

        if (registeredVersion != Util.getAppVersion(allInApplication)
                || !configOptions.getSenderId().equals(sharedProjectId)) {
            return new DeviceInfos(deviceId, true);
        }

        return new DeviceInfos(deviceId, false);
    }

    private void generateDeviceToken(DeviceInfos deviceInfos,
                                     ConfigurationListener configurationListener) {
        if (!isNetworkAvailable(configurationListener)) {
            return;
        }

        generateDeviceTokenAsync(deviceInfos, configurationListener);
    }

    private void generateDeviceTokenAsync(final DeviceInfos deviceInfos,
                                          final ConfigurationListener configurationListener) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                InstanceID instanceID = InstanceID.getInstance(allInApplication);

                String token = null;

                for (int attempts = 0; attempts < 3 &&
                        (token == null || TextUtils.isEmpty(token)); attempts++) {
                    try {
                        token = instanceID.getToken(configOptions.getSenderId(),
                                GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    } catch (IOException e) {
                        if (configurationListener != null) {
                            configurationListener.onError(
                                    new GenerateDeviceIdException(
                                            configOptions.getSenderId(), e.getMessage()));
                        } else {
                            e.printStackTrace();
                        }

                        return null;
                    }
                }

                prefManager.storeData(Preferences.DEVICE_ID, token);
                prefManager.storeData(Preferences.PROJECT_ID,
                        configOptions.getSenderId());

                return token;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (s != null && !TextUtils.isEmpty(s)) {
                    sendDeviceInfo(deviceInfos, configurationListener);
                }
            }
        }.execute();
    }

    private void configureNotifications(NotificationSettings notificationSettings) {
        if (notificationSettings != null) {
            prefManager.storeData(Preferences.ICON_NOTIFICATION,
                    notificationSettings.getIcon());
            prefManager.storeData(Preferences.WHITE_ICON_NOTIFICATION,
                    notificationSettings.getWhiteIcon());
            prefManager.storeData(Preferences.BACKGROUND_NOTIFICATION,
                    notificationSettings.getColorBackground());
        }
    }

    private void sendDeviceInfo(DeviceInfos deviceInfos, ConfigurationListener listener) {
        if (!isNetworkAvailable(listener)) {
            return;
        }

        sendDeviceInfoAsync(deviceInfos, listener);
    }

    private void sendDeviceInfoAsync(final DeviceInfos deviceInfos,
                                     final ConfigurationListener listener) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... params) {
                JSONObject data = new JSONObject();

                try {
                    data.put(HttpBody.DEVICE_TOKEN, AllInPush.getDeviceId(allInApplication));
                    data.put(HttpBody.PLATFORM, Parameters.ANDROID);

                    if (deviceInfos != null &&
                            !TextUtils.isEmpty(deviceInfos.getDeviceId()) &&
                            deviceInfos.isRenewId()) {
                        return HttpManager.post(Manager.this.getApplication(),
                                Route.DEVICE, data, new String[]{"update", deviceInfos.getDeviceId()});
                    } else {
                        return HttpManager.post(Manager.this.getApplication(),
                                Route.DEVICE, data, null);
                    }

                } catch (Exception e) {
                    return e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(Object object) {
                super.onPostExecute(object);

                if (object instanceof ResponseData) {
                    ResponseData responseData = (ResponseData) object;

                    if (!responseData.isSuccess()) {
                        if (listener != null) {
                            listener.onError(new WebServiceException(responseData.getMessage()));
                        }
                    } else {
                        String pushId = AllInPush.getDeviceId(allInApplication);

                        Map<String, String> map = new HashMap<>();
                        map.put("id_push", Util.md5(pushId));
                        map.put("push_id", pushId);
                        map.put("plataforma", Parameters.ANDROID);

                        AllInPush.sendList("Lista Padrao Push", map, listener);
                    }
                } else {
                    if (listener != null) {
                        listener.onError(new WebServiceException(String.valueOf(object)));
                    }
                }
            }
        }.execute();
    }

    /**
     * <b>Asynchronous</b> - Updates the e-mail in the database
     *
     * @param userEmail             E-mail that is registered in the database of AllIn
     * @param configurationListener Interface that returns success or error in the request
     */
    public void updateUserEmail(String userEmail, ConfigurationListener configurationListener) {
        if (!isNetworkAvailable(configurationListener) || Util.isNullOrClear(userEmail)) {
            return;
        }

        updateUserEmailAsync(userEmail, configurationListener);
    }

    private void updateUserEmailAsync(final String userEmail, final ConfigurationListener listener) {

        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... params) {
                JSONObject data = new JSONObject();

                try {
                    data.put(HttpBody.DEVICE_TOKEN, AllInPush.getDeviceId(allInApplication));
                    data.put(HttpBody.PLATFORM, Parameters.ANDROID);
                    data.put(HttpBody.USER_EMAIL, userEmail);

                    return HttpManager.post(
                            Manager.this.getApplication(), Route.EMAIL, data, null);
                } catch (Exception e) {
                    return e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(Object object) {
                super.onPostExecute(object);

                if (object instanceof ResponseData) {
                    ResponseData responseData = (ResponseData) object;

                    if (!responseData.isSuccess()) {
                        if (listener != null) {
                            listener.onError(new WebServiceException(responseData.getMessage()));
                        }
                    } else {
                        configureUserEmail(userEmail);

                        if (listener != null) {
                            listener.onFinish(responseData.getMessage());
                        }
                    }
                } else {
                    if (listener != null) {
                        listener.onError(new WebServiceException(String.valueOf(object)));
                    }
                }
            }
        }.execute();
    }

    /**
     * <b>Asynchronous</b> - Enable notifications on the server
     *
     * @param configurationListener Interface that returns success or error in the request
     */
    public void enable(ConfigurationListener configurationListener) {
        if (!isNetworkAvailable(configurationListener)) {
            return;
        }

        toggleAsync(true, configurationListener);
    }

    /**
     * <b>Asynchronous</b> - Disable notifications on the server
     *
     * @param configurationListener Interface that returns success or error in the request
     */
    public void disable(ConfigurationListener configurationListener) {
        if (!isNetworkAvailable(configurationListener)) {
            return;
        }

        toggleAsync(false, configurationListener);
    }

    private void toggleAsync(final boolean enable,
                             final ConfigurationListener configurationListener) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... params) {
                JSONObject data = new JSONObject();

                try {
                    data.put(HttpBody.DEVICE_TOKEN, AllInPush.getDeviceId(allInApplication));
                    data.put(HttpBody.PLATFORM, Parameters.ANDROID);

                    return HttpManager.post(Manager.this.getApplication(),
                            (enable ? Route.DEVICE_ENABLE : Route.DEVICE_DISABLE), data, null);
                } catch (Exception e) {
                    return e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(Object object) {
                super.onPostExecute(object);

                if (object instanceof ResponseData) {
                    ResponseData responseData = (ResponseData) object;

                    if (!responseData.isSuccess()) {
                        if (configurationListener != null) {
                            configurationListener.onError(
                                    new WebServiceException(responseData.getMessage()));
                        }
                    } else {
                        if (configurationListener != null) {
                            configurationListener.onFinish(responseData.getMessage());
                        }
                    }
                } else {
                    if (configurationListener != null) {
                        configurationListener.onError(
                                new WebServiceException(String.valueOf(object)));
                    }
                }
            }
        }.execute();
    }

    public void logout(ConfigurationListener configurationListener) {
        if (!isNetworkAvailable(configurationListener)) {
            return;
        }

        logoutAsync(configurationListener);
    }

    private void logoutAsync(final ConfigurationListener configurationListener) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... params) {
                JSONObject data = new JSONObject();

                try {
                    data.put(HttpBody.DEVICE_TOKEN, AllInPush.getDeviceId(allInApplication));
                    data.put(HttpBody.USER_EMAIL, AllInPush.getUserEmail(allInApplication));

                    return HttpManager.post(Manager.this.getApplication(),
                            Route.DEVICE_LOGOUT, data, null);
                } catch (Exception e) {
                    return e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(Object object) {
                super.onPostExecute(object);

                if (object instanceof ResponseData) {
                    ResponseData responseData = (ResponseData) object;

                    if (!responseData.isSuccess()) {
                        if (configurationListener != null) {
                            configurationListener.onError(
                                    new WebServiceException(responseData.getMessage()));
                        }
                    } else {
                        if (configurationListener != null) {
                            configurationListener.onFinish(responseData.getMessage());
                        }
                    }
                } else {
                    if (configurationListener != null) {
                        configurationListener.onError(
                                new WebServiceException(String.valueOf(object)));
                    }
                }
            }
        }.execute();
    }

    /**
     * <b>Asynchronous</b> - Checks whether notifications are enabled on the server
     *
     * @param configurationListener Interface that returns success or error in the request
     */
    public void deviceIsEnable(ConfigurationListener configurationListener) {
        if (!isNetworkAvailable(configurationListener)) {
            return;
        }

        deviceIsEnableAsync(configurationListener);
    }

    private void deviceIsEnableAsync(final ConfigurationListener configurationListener) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... params) {

                try {
                    return HttpManager.get(Manager.this.getApplication(),
                            Route.DEVICE_STATUS,
                            new String[]{Parameters.ANDROID, AllInPush.getDeviceId(allInApplication)});
                } catch (Exception e) {
                    return e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(Object object) {
                super.onPostExecute(object);

                if (object instanceof ResponseData) {
                    ResponseData responseData = (ResponseData) object;

                    if (!responseData.isSuccess()) {
                        if (configurationListener != null) {
                            configurationListener.onError(
                                    new WebServiceException(responseData.getMessage()));
                        }
                    } else {
                        if (configurationListener != null) {
                            configurationListener.onFinish(
                                    responseData.getMessage().equalsIgnoreCase("enabled"));
                        }
                    }
                } else {
                    if (configurationListener != null) {
                        configurationListener.onError(
                                new WebServiceException(String.valueOf(object)));
                    }
                }
            }
        }.execute();
    }

    /**
     * <b>Asynchronous</b> - Returns the HTML campaign created
     *
     * @param id                    Template ID that the push notification returns
     * @param configurationListener Interface that returns success or error in the request
     */
    public void getHtmlTemplate(int id, ConfigurationListener configurationListener) {
        if (!isNetworkAvailable(configurationListener)) {
            return;
        }

        getHtmlTemplateAsync(id, configurationListener);
    }

    private void getHtmlTemplateAsync(final int id,
                                      final ConfigurationListener configurationListener) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... params) {

                try {
                    return HttpManager.get(Manager.this.getApplication(),
                            Route.CAMPAIGN, new String[]{String.valueOf(id)});
                } catch (Exception e) {
                    return e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(Object object) {
                super.onPostExecute(object);

                if (object instanceof ResponseData) {
                    ResponseData responseData = (ResponseData) object;

                    if (!responseData.isSuccess()) {
                        if (configurationListener != null) {
                            configurationListener.onError(
                                    new WebServiceException(responseData.getMessage()));
                        }
                    } else {
                        if (configurationListener != null) {
                            configurationListener.onFinish(responseData.getMessage());
                        }
                    }
                } else {
                    if (configurationListener != null) {
                        configurationListener.onError(
                                new WebServiceException(String.valueOf(object)));
                    }
                }
            }
        }.execute();
    }

    /**
     * <b>Asynchronous</b> - Shipping to list
     *
     * @param name                  Mailing list that will be sent
     * @param columns               Columns of list to be recorded
     * @param values                Values of list to be recorded
     * @param configurationListener Interface that returns success or error in the request
     */
    public void sendList(final String name, final String columns,
                         final String values, final ConfigurationListener configurationListener) {
        if (!isNetworkAvailable(configurationListener)) {
            return;
        }

        if (Util.isNullOrClear(name) || Util.isNullOrClear(columns) || Util.isNullOrClear(values)) {
            return;
        }

        if (!isNetworkAvailable(configurationListener)) {
            return;
        }

        sendDefaultListAsync(name, columns, values, configurationListener);
    }

    private void sendDefaultListAsync(final String name, final String columns,
                                      final String values, final ConfigurationListener listener) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... params) {
                JSONObject data = new JSONObject();

                try {
                    if (columns.endsWith(";")) {
                        data.put(HttpBody.CAMPOS, columns.substring(0, columns.length() - 1));
                    } else {
                        data.put(HttpBody.CAMPOS, columns);
                    }

                    if (values.endsWith(";")) {
                        data.put(HttpBody.VALOR, values.substring(0, values.length() - 1));
                    } else {
                        data.put(HttpBody.VALOR, values);
                    }

                    data.put(HttpBody.NAME_LIST, name);

                    return HttpManager.post(
                            Manager.this.getApplication(), Route.ADD_LIST, data, null);
                } catch (Exception e) {
                    return e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(Object object) {
                super.onPostExecute(object);

                if (object instanceof ResponseData) {
                    ResponseData responseData = (ResponseData) object;

                    if (!responseData.isSuccess()) {
                        if (listener != null) {
                            listener.onError(new WebServiceException(responseData.getMessage()));
                        }
                    } else {
                        if (listener != null) {
                            listener.onFinish(responseData.getMessage());
                        }
                    }
                } else {
                    if (listener != null) {
                        listener.onError(new WebServiceException(String.valueOf(object)));
                    }
                }
            }
        }.execute();
    }

    /**
     * <b>Asynchronous</b> - Register push the event (According to the enum Action)
     *
     * @param configurationListener Interface that returns success or error in the request
     */
    public void notificationCampaign(final int idCampaign,
                                           final ConfigurationListener configurationListener) {
        AllInLocation.initialize(allInApplication, new OnAllInLocationChange() {
            @Override
            public void locationFound(double latitude, double longitude) {
                registerNotification(createData(idCampaign, latitude, longitude));
            }

            @Override
            public void locationNotFound() {
                registerNotification(createData(idCampaign));
            }

            private void registerNotification(JSONObject jsonObject) {
                notificationCampaignAsync(jsonObject, configurationListener);
            }
        });
    }

    private void notificationCampaignAsync(final JSONObject data,
                                             final ConfigurationListener configurationListener) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... params) {

                try {
                    return HttpManager.post(Manager.this.getApplication(),
                            Route.NOTIFICATION_CAMPAIGN, data, null, true);
                } catch (Exception e) {
                    return e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(Object object) {
                super.onPostExecute(object);

                if (object instanceof ResponseData) {
                    ResponseData responseData = (ResponseData) object;

                    if (!responseData.isSuccess()) {
                        if (configurationListener != null) {
                            configurationListener.onError(
                                    new WebServiceException(responseData.getMessage()));
                        }
                    } else {
                        if (configurationListener != null) {
                            configurationListener.onFinish(responseData.getMessage());
                        }
                    }
                } else {
                    if (configurationListener != null) {
                        configurationListener.onError(
                                new WebServiceException(String.valueOf(object)));
                    }
                }
            }
        }.execute();
    }

    private JSONObject createData(int idCampaign) {
        return createData(idCampaign, 0.0, 0.0);
    }

    private JSONObject createData(int idCampaign, double latitude, double longitude) {
        JSONObject data = new JSONObject();

        try {
            data.put(HttpBody.PLATFORM, Parameters.ANDROID);
            data.put(HttpBody.DEVICE_TOKEN, AllInPush.getDeviceId(allInApplication));
            data.put(HttpBody.ID, String.valueOf(idCampaign));
            data.put(HttpBody.DATE, Util.currentDate("yyyy-MM-dd HH:mm:ss"));

            if (latitude != 0.0 && longitude != 0.0) {
                data.put(HttpBody.LATITUDE, String.valueOf(latitude));
                data.put(HttpBody.LONGITUDE, String.valueOf(longitude));
            }
        } catch (Exception e) {
            data = null;
        }

        return data;
    }

    public void notificationTransactional(final int idSend,
                                            final ConfigurationListener configurationListener) {
        if (!isNetworkAvailable(configurationListener)) {
            return;
        }

        notificationTransactionalAsync(idSend, configurationListener);
    }

    private void notificationTransactionalAsync(final int idSend,
                                             final ConfigurationListener configurationListener) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... params) {

                JSONObject data = new JSONObject();

                try {
                    data.put(HttpBody.ID, idSend);
                    data.put(HttpBody.DATE, Util.currentDate("yyyy-MM-dd HH:mm:ss"));

                    return HttpManager.post(Manager.this.getApplication(),
                            Route.NOTIFICATION_TRANSACTIONAL, data, null, true);
                } catch (Exception e) {
                    return e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(Object object) {
                super.onPostExecute(object);

                if (object instanceof ResponseData) {
                    ResponseData responseData = (ResponseData) object;

                    if (!responseData.isSuccess()) {
                        if (configurationListener != null) {
                            configurationListener.onError(
                                    new WebServiceException(responseData.getMessage()));
                        }
                    } else {
                        if (configurationListener != null) {
                            configurationListener.onFinish(responseData.getMessage());
                        }
                    }
                } else {
                    if (configurationListener != null) {
                        configurationListener.onError(
                                new WebServiceException(String.valueOf(object)));
                    }
                }
            }
        }.execute();
    }

    public Application getApplication() {
        return allInApplication;
    }

    /**
     * @return Settings have been loaded and recorded information
     */
    public boolean isConfigurationLoaded() {
        return allInApplication != null && configOptions != null && prefManager != null;
    }

    /**
     * Writes the user email in SharedPreferences
     *
     * @param userEmail User e-mail
     */
    public void configureUserEmail(String userEmail) {
        prefManager.storeData(Preferences.USER_EMAIL, userEmail);
    }

    private void validateParams(AllInApplication allinApplication, ConfigurationOptions configOptions)
            throws NotNullAttributeOrPropertyException {
        if (allinApplication == null) {
            throw new NotNullAttributeOrPropertyException("allinApplication", "configure");
        }
        if (configOptions == null) {
            throw new NotNullAttributeOrPropertyException("configOptions", "configure");
        }
    }

    private boolean isNetworkAvailable(ConfigurationListener listener) {
        if (Util.isNetworkAvailable(this.allInApplication)) {
            return true;
        } else {
            listener.onError(new NetworkException(null, "Internet não está disponível"));

            return false;
        }
    }
}
