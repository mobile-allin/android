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
        new NotificationCampaignTask(idCampaign, date).execute();
    }

    public void sendTransactional(int idSend, String date) {
        new NotificationTransactionalTask(idSend, date).execute();
    }


}