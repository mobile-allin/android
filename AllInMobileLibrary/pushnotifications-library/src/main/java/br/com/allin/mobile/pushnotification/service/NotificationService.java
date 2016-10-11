package br.com.allin.mobile.pushnotification.service;

import android.content.Context;

import br.com.allin.mobile.pushnotification.gcm.AllInLocation;
import br.com.allin.mobile.pushnotification.interfaces.OnAllInLocationChange;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;
import br.com.allin.mobile.pushnotification.task.NotificationCampaignTask;
import br.com.allin.mobile.pushnotification.task.NotificationTransactionalTask;

/**
 * Service class for notification click
 */
public class NotificationService {
    public void sendCampaign(final int idCampaign, final String date, final Context context) {
        AllInLocation.initialize(context, new OnAllInLocationChange() {
            @Override
            public void locationFound(double latitude, double longitude) {
                new NotificationCampaignTask(idCampaign, date, latitude, longitude, context).execute();
            }

            @Override
            public void locationNotFound() {
                new NotificationCampaignTask(idCampaign, date, context).execute();
            }
        });
    }

    public void sendTransactional(final int idSend, final String date, final Context context) {
        new NotificationTransactionalTask(idSend, date, context).execute();
    }
}
