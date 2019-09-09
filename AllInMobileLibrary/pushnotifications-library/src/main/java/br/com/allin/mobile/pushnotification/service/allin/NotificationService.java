package br.com.allin.mobile.pushnotification.service.allin;

import br.com.allin.mobile.pushnotification.task.allin.NotificationCampaignTask;
import br.com.allin.mobile.pushnotification.task.allin.NotificationTransactionalTask;

/**
 * Service class for notification click
 */
public class NotificationService {
    public void sendCampaign(final int idCampaign, final String date) {
        new NotificationCampaignTask(idCampaign, date).execute();
    }

    public void sendTransactional(int idSend, String date) {
        new NotificationTransactionalTask(idSend, date).execute();
    }
}