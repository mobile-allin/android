package br.com.allin.mobile.pushnotification.entity.allin;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
    AINotification object from local database
 */
@Entity(tableName = "notification")
public class AINotification {
    @PrimaryKey
    private long idMessage;
    private String body;
    private String title;

    public AINotification() {
    }

    @Ignore
    public AINotification(long idMessage, String title, String body) {
        this.idMessage = idMessage;
        this.title = title;
        this.body = body;
    }

    public long getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(long idMessage) {
        this.idMessage = idMessage;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
