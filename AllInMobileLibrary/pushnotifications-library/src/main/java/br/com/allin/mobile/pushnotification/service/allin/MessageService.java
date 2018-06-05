package br.com.allin.mobile.pushnotification.service.allin;

import java.util.List;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.dao.AlliNDatabase;
import br.com.allin.mobile.pushnotification.dao.MessageDAO;
import br.com.allin.mobile.pushnotification.entity.allin.AIMessage;

/**
 * Created by lucasrodrigues on 06/04/17.
 */

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = AlliNDatabase.get().messageTable();
    }

    public long addMessage(AIMessage message) {
        return messageDAO.insert(message);
    }

    public void deleteMessage(long id) {
        messageDAO.deleteById(id);
    }

    public boolean hasBeenRead(long id) {
        return messageDAO.hasBeenRead(id);
    }

    public List<AIMessage> getMessages() {
        return messageDAO.get();
    }
}