package br.com.allin.mobile.pushnotification.service.allin;

import android.content.Context;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.entity.allin.AINotification;
import br.com.allin.mobile.pushnotification.helper.PreferencesManager;
import br.com.allin.mobile.pushnotification.helper.Util;
import br.com.allin.mobile.pushnotification.identifiers.PreferenceIdentifier;

/**
 * Service class for push configuration
 */
public class ConfigurationService {
    private AINotification notification;

    public ConfigurationService(AINotification notification) {
        this.notification = notification;
    }

    public void init() {
        new CacheService().sync();

        if (this.notification != null) {
            int icon = this.notification.getIcon();
            int whiteIcon = this.notification.getWhiteIcon();
            int background = this.notification.getBackground();

            Context context = AlliNPush.getInstance().getContext();
            PreferencesManager preferences = new PreferencesManager(context);

            preferences.storeData(PreferenceIdentifier.ICON_NOTIFICATION, icon);
            preferences.storeData(PreferenceIdentifier.WHITE_ICON_NOTIFICATION, whiteIcon);
            preferences.storeData(PreferenceIdentifier.BACKGROUND_NOTIFICATION, background);
        }
    }
}
