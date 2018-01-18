package br.com.allin.mobile.pushnotification.service;

import android.content.Context;

import br.com.allin.mobile.pushnotification.gcm.AllInLocation;
import br.com.allin.mobile.pushnotification.interfaces.OnAllInLocationChange;
import br.com.allin.mobile.pushnotification.task.NotificationCampaignTask;
import br.com.allin.mobile.pushnotification.task.NotificationTransactionalTask;

/**
 * Service class for notification click
 */
public class NotificationService {
    public void sendCampaign(final int idCampaign, final String date) {
        AllInLocation.initialize(new OnAllInLocationChange() {
            @Override
            public void locationFound(double latitude, double longitude) {
                new NotificationCampaignTask(idCampaign, date, latitude, longitude).execute();
            }

            @Override
            public void locationNotFound() {
                new NotificationCampaignTask(idCampaign, date).execute();
            }
        });
    }

    public void sendTransactional(int idSend, String date, Context context) {
        new NotificationTransactionalTask(idSend, date).execute();
    }
}
