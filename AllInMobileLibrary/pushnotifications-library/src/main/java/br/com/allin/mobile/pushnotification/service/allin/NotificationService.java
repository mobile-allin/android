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
<<<<<<< HEAD
    public static List<AINotification> getList() {
        return AlliNDatabase.get().notificationTable().get();
    }

    public static void insert(String idMessage, String title, String body) {
        AlliNDatabase.get().notificationTable().insert(new AINotification(idMessage, title, body));
    }


    public static void sendCampaign(String idCampaign, String date) {
=======

    private NotificationDAO notificationDAO;

    public NotificationService() {
        this.notificationDAO = AlliNDatabase.get().notificationTable();
    }

    public List<AINotification> getList() {
        return this.notificationDAO.get();
    }

    public void insert(int idMessage, String title, String body) {
        this.notificationDAO.insert(new AINotification(idMessage, title, body));
    }
    
    public void sendCampaign(final int idCampaign, final String date) {
>>>>>>> 350efc96fc364224c48e0b253b6f750ba8f711ce
        new NotificationCampaignTask(idCampaign, date).execute();
    }

    public static void sendTransactional(String idSend, String date) {
        new NotificationTransactionalTask(idSend, date).execute();
    }


}