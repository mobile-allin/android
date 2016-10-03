package br.com.allin.mobile.pushnotification.service;

import android.content.Context;

import br.com.allin.mobile.pushnotification.gcm.AllInLocation;
import br.com.allin.mobile.pushnotification.interfaces.OnAllInLocationChange;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;
import br.com.allin.mobile.pushnotification.task.NotificationCampaignTask;
import br.com.allin.mobile.pushnotification.task.NotificationTransactionalTask;

/**
 * Created by lucasrodrigues on 10/3/16.
 */

public class NotificationService {
    public void sendCampaign(final int idCampaign, final Context context) {
        this.sendCampaign(idCampaign, context, null);
    }

    public void sendCampaign(final int idCampaign,
                             final Context context, final OnRequest onRequest) {
        AllInLocation.initialize(context, new OnAllInLocationChange() {
            @Override
            public void locationFound(double latitude, double longitude) {
                new NotificationCampaignTask(idCampaign,
                        latitude, longitude, context, onRequest).execute();
            }

            @Override
            public void locationNotFound() {
                new NotificationCampaignTask(idCampaign, context, onRequest).execute();
            }
        });
    }

    public void sendTransactional(final int idSend, final Context context) {
        this.sendTransactional(idSend, context, null);
    }

    public void sendTransactional(final int idSend,
                                  final Context context, final OnRequest onRequest) {
        new NotificationTransactionalTask(idSend, context, onRequest).execute();
    }
}
