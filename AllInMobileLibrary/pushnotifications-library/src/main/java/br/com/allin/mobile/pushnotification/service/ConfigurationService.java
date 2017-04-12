package br.com.allin.mobile.pushnotification.service;

import android.content.Context;
import android.text.TextUtils;

import br.com.allin.mobile.pushnotification.AllInApplication;
import br.com.allin.mobile.pushnotification.SharedPreferencesManager;
import br.com.allin.mobile.pushnotification.constants.PreferencesConstants;
import br.com.allin.mobile.pushnotification.entity.ConfigurationEntity;
import br.com.allin.mobile.pushnotification.entity.DeviceEntity;
import br.com.allin.mobile.pushnotification.entity.NotificationEntity;
import br.com.allin.mobile.pushnotification.exception.NotNullAttributeOrPropertyException;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;

/**
 * Service class for push configuration
 */
public class ConfigurationService {
    private OnRequest onRequest;
    private ConfigurationEntity configurationEntity;
    private Context context;
    private NotificationEntity notificationEntity;
    private SharedPreferencesManager sharedPreferencesManager;

    public ConfigurationService(AllInApplication allInApplication,
                                ConfigurationEntity configurationEntity,
                                OnRequest onRequest)
                                throws NotNullAttributeOrPropertyException {

        if (allInApplication == null) {
            throw new NotNullAttributeOrPropertyException("allinApplication", "configure");
        }

        if (configurationEntity == null) {
            throw new NotNullAttributeOrPropertyException("configOptions", "configure");
        }

        this.onRequest = onRequest;
        this.configurationEntity = configurationEntity;
        this.context = allInApplication;
        this.notificationEntity = configurationEntity.getNotificationEntity();
        this.sharedPreferencesManager = new SharedPreferencesManager(context);
    }

    public void init() {
        new CacheService(this.context).sync();

        if (notificationEntity != null) {
            sharedPreferencesManager.storeData(PreferencesConstants.KEY_ICON_NOTIFICATION,
                    notificationEntity.getIcon());
            sharedPreferencesManager.storeData(PreferencesConstants.KEY_WHITE_ICON_NOTIFICATION,
                    notificationEntity.getWhiteIcon());
            sharedPreferencesManager.storeData(PreferencesConstants.KEY_BACKGROUND_NOTIFICATION,
                    notificationEntity.getColorBackground());
        }

        DeviceEntity deviceEntity =
                new DeviceService(context).getDeviceInfos(this.configurationEntity.getSenderId());

        if (deviceEntity == null ||
                TextUtils.isEmpty(deviceEntity.getDeviceId()) || deviceEntity.isRenewId()) {
            new GCMService(deviceEntity, this.context,
                    this.configurationEntity, onRequest).execute();
        } else {
            new DeviceService(this.context, onRequest).sendDevice(deviceEntity);
        }
    }
}
