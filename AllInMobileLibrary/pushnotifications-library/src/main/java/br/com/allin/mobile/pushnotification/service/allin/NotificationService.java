package br.com.allin.mobile.pushnotification.service.allin;

import java.util.List;

import br.com.allin.mobile.pushnotification.dao.AlliNDatabase;
import br.com.allin.mobile.pushnotification.entity.allin.AINotification;
import br.com.allin.mobile.pushnotification.task.allin.NotificationViewTask;

/**
 * Service class for notification click
 */
public class NotificationService {
    public static List<AINotification> getList() {
        return AlliNDatabase.get().notificationTable().get();
    }

    public static void insert(String idMessage, String title, String body) {
        AlliNDatabase.get().notificationTable().insert(new AINotification(idMessage, title, body));
    }

    public static void sendView(String idCampaign) {
        new NotificationViewTask(idCampaign).execute();
    }
}