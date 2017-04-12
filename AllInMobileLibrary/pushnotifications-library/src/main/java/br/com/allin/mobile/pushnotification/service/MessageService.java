package br.com.allin.mobile.pushnotification.service;

import android.content.Context;

import java.util.List;

import br.com.allin.mobile.pushnotification.dao.MessageDAO;
import br.com.allin.mobile.pushnotification.entity.MessageEntity;

/**
 * Created by lucasrodrigues on 06/04/17.
 */

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(Context context) {
        this.messageDAO = new MessageDAO(context);
    }

    public long addMessage(MessageEntity messageEntity) {
        return messageDAO.insert(messageEntity);
    }

    public boolean deleteMessage(int id) {
        return messageDAO.delete(id);
    }

    public boolean messageHasBeenRead(int id) {
        return messageDAO.messageHasBeenRead(id);
    }

    public List<MessageEntity> getMessages() {
        return messageDAO.getAll();
    }
}
