package br.com.allin.mobile.pushnotification.entity.allin;

import android.arch.persistence.room.Entity;
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
    public AICache(String url, String json) {
        this.url = url;
        this.json = json;
    }

    /**
      @return Recorded cache Id
    */
    public long getId() {
        return id;
    }

    /**
       @return URL cache recorded
     */
    public String getUrl() {
        return url;
    }

    /**
       @return JSON recorded cache
     */
    public String getJson() {
        return json;
    }
}
