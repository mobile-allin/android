package br.com.allin.mobile.pushnotification.service.allin;

import android.text.TextUtils;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.helper.PreferencesManager;
import br.com.allin.mobile.pushnotification.constants.PreferencesConstant;
import br.com.allin.mobile.pushnotification.entity.allin.AIConfiguration;
import br.com.allin.mobile.pushnotification.entity.allin.AIDevice;
import br.com.allin.mobile.pushnotification.entity.allin.AINotification;

/**
 * Service class for push configuration
 */
public class ConfigurationService {
    private AIConfiguration AIConfiguration;
    private AINotification AINotification;
    private PreferencesManager preferencesManager;

    public ConfigurationService(AIConfiguration AIConfiguration) {
        this.AIConfiguration = AIConfiguration;
        this.AINotification = AIConfiguration.getNotification();
        this.preferencesManager = new PreferencesManager(AlliNPush.getInstance().getContext());
    }

    public void init() {
        new CacheService().sync();

        if (AINotification != null) {
            preferencesManager.storeData(
                    PreferencesConstant.ICON_NOTIFICATION, AINotification.getIcon());
            preferencesManager.storeData(
                    PreferencesConstant.WHITE_ICON_NOTIFICATION, AINotification.getWhiteIcon());
            preferencesManager.storeData(
                    PreferencesConstant.BACKGROUND_NOTIFICATION, AINotification.getBackground());
        }

        DeviceService deviceService = new DeviceService();
        AIDevice device = deviceService.getDeviceInfos(AIConfiguration.getSenderId());

        if (device == null || TextUtils.isEmpty(device.getDeviceId()) || device.isRenewId()) {
            GCMService gcmService = new GCMService(device, AIConfiguration);
            gcmService.execute();
        } else {
            deviceService.sendDevice(device);
        }
    }
}
