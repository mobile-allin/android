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

    public void addMessage(MessageEntity messageEntity) {
        this.messageDAO.insert(messageEntity);
    }

    public void deleteMessage(int id) {
        this.messageDAO.delete(id);
    }

    public List<MessageEntity> getMessages() {
        return this.messageDAO.getAll();
    }
}
