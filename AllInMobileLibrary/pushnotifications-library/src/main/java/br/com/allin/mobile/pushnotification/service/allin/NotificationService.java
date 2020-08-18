package br.com.allin.mobile.pushnotification.service.allin;

import java.util.List;

import br.com.allin.mobile.pushnotification.dao.AlliNDatabase;
import br.com.allin.mobile.pushnotification.dao.NotificationDAO;
import br.com.allin.mobile.pushnotification.entity.allin.AINotification;
import br.com.allin.mobile.pushnotification.task.allin.NotificationCampaignTask;
import br.com.allin.mobile.pushnotification.task.allin.NotificationTransactionalTask;

/**
 * Service class for notification click
 */
public class NotificationService {
    public static List<AINotification> getList() {
        return AlliNDatabase.get().notificationTable().get();
    }

    public static void insert(int idMessage, String title, String body) {
        AlliNDatabase.get().notificationTable().insert(new AINotification(idMessage, title, body));
    }


    public static void sendCampaign(int idCampaign, String date) {
        new NotificationCampaignTask(idCampaign, date).execute();
    }

    public static void sendTransactional(int idSend, String date) {
        new NotificationTransactionalTask(idSend, date).execute();
    }
}