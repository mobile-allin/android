package br.com.allin.mobile.pushnotification.service.allin;

import br.com.allin.mobile.pushnotification.task.allin.MessageTask;

public class MessageService {
    public static void markAsRead(int notificationId) {
        new MessageTask(notificationId).execute();
    }
}