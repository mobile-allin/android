package br.com.allin.mobile.pushnotification.service;

import android.text.TextUtils;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.helper.SharedPreferencesManager;
import br.com.allin.mobile.pushnotification.constants.PreferencesConstants;
import br.com.allin.mobile.pushnotification.entity.ConfigurationEntity;
import br.com.allin.mobile.pushnotification.entity.DeviceEntity;
import br.com.allin.mobile.pushnotification.entity.NotificationEntity;

/**
 * Service class for push configuration
 */
public class ConfigurationService {
    private ConfigurationEntity configurationEntity;
    private NotificationEntity notificationEntity;
    private SharedPreferencesManager sharedPreferencesManager;

    public ConfigurationService(ConfigurationEntity configurationEntity) {
        this.configurationEntity = configurationEntity;
        this.notificationEntity = configurationEntity.getNotification();
        this.sharedPreferencesManager = new SharedPreferencesManager(AlliNPush.getInstance().getContext());
    }

    public void init() {
        new CacheService().sync();

        if (notificationEntity != null) {
            sharedPreferencesManager.storeData(
                    PreferencesConstants.KEY_ICON_NOTIFICATION, notificationEntity.getIcon());
            sharedPreferencesManager.storeData(
                    PreferencesConstants.KEY_WHITE_ICON_NOTIFICATION, notificationEntity.getWhiteIcon());
            sharedPreferencesManager.storeData(
                    PreferencesConstants.KEY_BACKGROUND_NOTIFICATION, notificationEntity.getBackground());
        }

        DeviceService deviceService = new DeviceService();
        DeviceEntity deviceEntity = deviceService.getDeviceInfos(configurationEntity.getSenderId());

        if (deviceEntity == null || TextUtils
                .isEmpty(deviceEntity.getDeviceId()) || deviceEntity.isRenewId()) {
            new GCMService(deviceEntity, configurationEntity).execute();
        } else {
            new DeviceService().sendDevice(deviceEntity);
        }
    }
}
