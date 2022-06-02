package br.com.allin.mobile.pushnotification.entity.allin;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

<<<<<<< HEAD
import java.util.UUID;

/**
 * AINotification object from local database
 */
@Entity(tableName = "notifications_history")
public class AINotification {
    @PrimaryKey
    @NonNull
    private String idMessage = UUID.randomUUID().toString();
=======
/**
    AINotification object from local database
 */
@Entity(tableName = "notification")
public class AINotification {
    @PrimaryKey
    private long idMessage;
>>>>>>> 350efc96fc364224c48e0b253b6f750ba8f711ce
    private String body;
    private String title;

    public AINotification() {
    }

    @Ignore
<<<<<<< HEAD
    public AINotification(@NonNull String idMessage, String title, String body) {
=======
    public AINotification(long idMessage, String title, String body) {
>>>>>>> 350efc96fc364224c48e0b253b6f750ba8f711ce
        this.idMessage = idMessage;
        this.title = title;
        this.body = body;
    }

<<<<<<< HEAD
    @NonNull
    public String getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(@NonNull String idMessage) {
=======
    public long getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(long idMessage) {
>>>>>>> 350efc96fc364224c48e0b253b6f750ba8f711ce
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
<<<<<<< HEAD

=======
>>>>>>> 350efc96fc364224c48e0b253b6f750ba8f711ce
