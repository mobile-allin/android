package br.com.allin.mobile.pushnotification.service.allin;

import java.util.List;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.dao.MessageDAO;
import br.com.allin.mobile.pushnotification.entity.allin.AlMessage;

/**
 * Created by lucasrodrigues on 06/04/17.
 */

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO(AlliNPush.getInstance().getContext());
    }

    public long addMessage(AlMessage alMessage) {
        return messageDAO.insert(alMessage);
    }

    public boolean deleteMessage(int id) {
        return messageDAO.delete(id);
    }

    public boolean messageHasBeenRead(int id) {
        return messageDAO.messageHasBeenRead(id);
    }

    public List<AlMessage> getMessages() {
        return messageDAO.getAll();
    }
}