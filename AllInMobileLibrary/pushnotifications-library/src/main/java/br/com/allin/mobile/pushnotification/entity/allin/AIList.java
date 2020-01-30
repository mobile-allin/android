package br.com.allin.mobile.pushnotification.entity.allin;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
    AICache object from local database
 */
@Entity(tableName = "list")
public class AIList {
    @PrimaryKey
    @NonNull
    private String md5;

    public AIList(String md5) {
        this.md5 = md5;
    }

    public String getMd5() {
        return md5;
    }
}
