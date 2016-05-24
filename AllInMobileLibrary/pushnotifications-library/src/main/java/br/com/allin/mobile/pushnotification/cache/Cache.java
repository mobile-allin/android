package br.com.allin.mobile.pushnotification.cache;

/**
 * Cache object from local database
 */
public class Cache {
    private int id;
    private String url;
    private String json;

    /**
     * Initializes the object by setting the URL and JSON (the ID is generated automatically in the local database)
     * 
     * @param id request attempt ID
     * @param url URL attempt request
     * @param json JSON attempt request
    */
    public Cache(int id, String url, String json) {
        this.id = id;
        this.url = url;
        this.json = json;
    }

    /**
     * @return Recorded cache Id
    */
    public int getId() {
        return id;
    }

    /**
     * @return URL cache recorded
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return JSON recorded cache
     */
    public String getJson() {
        return json;
    }
}
