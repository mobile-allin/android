package br.com.allin.mobile.pushnotification.entity.allin;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
    AICache object from local database
 */
@Entity(tableName = "cache")
public class AICache {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String url;
    private String json;

    public AICache() {
    }

    /**
      Initializes the object by setting the URL and JSON (the ID is generated automatically in the local database)

      @param url URL attempt request
      @param json JSON attempt request
    */
    @Ignore
    public AICache(String url, String json) {
        this.url = url;
        this.json = json;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
