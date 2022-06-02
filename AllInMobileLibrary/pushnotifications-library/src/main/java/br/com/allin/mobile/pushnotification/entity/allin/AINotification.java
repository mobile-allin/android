package br.com.allin.mobile.pushnotification.entity.allin;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.UUID;

/**
 * AINotification object from local database
 */
@Entity(tableName = "notifications_history")
public class AINotification {
    @PrimaryKey
    @NonNull
    private String idMessage = UUID.randomUUID().toString();
    private String body;
    private String title;

    public AINotification() {
    }

    @Ignore
    public AINotification(@NonNull String idMessage, String title, String body) {
        this.idMessage = idMessage;
        this.title = title;
        this.body = body;
    }

    @NonNull
    public String getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(@NonNull String idMessage) {
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

