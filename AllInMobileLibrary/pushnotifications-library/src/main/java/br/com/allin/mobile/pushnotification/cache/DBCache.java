package br.com.allin.mobile.pushnotification.cache;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.allin.mobile.pushnotification.exception.WebServiceException;
import br.com.allin.mobile.pushnotification.http.HttpManager;
import br.com.allin.mobile.pushnotification.listener.ConfigurationListener;
import br.com.allin.mobile.pushnotification.model.ResponseData;

/**
 * Class that manages the Cache database requests
 */
public class DBCache {
    private static String TABLE_NAME = "cache";
    private static String DB_FIELD_ID = "id";
    private static String DB_FIELD_URL = "url";
    private static String DB_FIELD_JSON = "json";
    private static String DB_NAME = "allin.db";

    private static String CREATE_TABLE_CACHE = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s VARCHAR, %s VARCHAR);", TABLE_NAME, DB_FIELD_ID, DB_FIELD_URL, DB_FIELD_JSON);
    private static String INSERT_CACHE = String.format("INSERT INTO %s (%s, %s) VALUES ('#VALUE1', '#VALUE2');", TABLE_NAME, DB_FIELD_URL, DB_FIELD_JSON);
    private static String QUERY_CACHE = String.format("SELECT %s, %s, %s FROM %s", DB_FIELD_ID, DB_FIELD_URL, DB_FIELD_JSON, TABLE_NAME);
    private static String DELETE_CACHE = String.format("DELETE FROM %s WHERE %s = #VALUE1", TABLE_NAME, DB_FIELD_ID);

    private DBCache() {
    }

    private Context context;
    private SQLiteDatabase sqliteDatabase;

    public void setContext(Context context) {
        this.context = context;
    }

    private static DBCache cache;

    /**
    * Creates (if there) and opens the connection to the database cache data
    *
    * @return Object instance
    */
    public static DBCache getInstance(Context context) {
        if (cache == null) {
            cache = new DBCache();
        }

        cache.setContext(context);
        cache.openDatabase();

        return cache;
    }

    private void openDatabase() {
        sqliteDatabase = context.openOrCreateDatabase(DBCache.DB_NAME, Context.MODE_PRIVATE, null);

        createTable();
    }

    private void closeDatabase() {
        if (sqliteDatabase != null) {
            sqliteDatabase.close();
        }
    }

    private void createTable() {
        if (sqliteDatabase != null) {
            sqliteDatabase.execSQL(CREATE_TABLE_CACHE);
        }
    }

    /**
     * Inserts in the database URL and JSON request (the ID is generated automatically by the local database)
     *
     * @param url URL attempt request
     * @param json JSON attempt request
    */
    public void insert(String url, String json) {
        if (sqliteDatabase != null) {
            sqliteDatabase.execSQL(DBCache.INSERT_CACHE.replace("#VALUE1", url).replace("#VALUE2", json));
        }

        closeDatabase();
    }

    private void delete(int id) {
        if (sqliteDatabase != null) {
            sqliteDatabase.execSQL(DBCache.DELETE_CACHE.replace("#VALUE1", String.valueOf(id)));
        }

        closeDatabase();
    }

    /**
     * Search all requests not made yet and performs according to the recorded information
    */
    public  void sync() {
        List<Cache> cacheList = getAll();

        for (Cache cache : cacheList) {
            sync(cache);
        }
    }

    private List<Cache> getAll() {
        List<Cache> cacheList = new ArrayList<>();

        if (sqliteDatabase != null) {
            Cursor cursor = sqliteDatabase.rawQuery(DBCache.QUERY_CACHE, null);

            if (cursor != null) {
                while (!cursor.isAfterLast()) {
                    int id = cursor.getInt(cursor.getColumnIndex(DB_FIELD_ID));
                    String url = cursor.getString(cursor.getColumnIndex(DB_FIELD_URL));
                    String json = cursor.getString(cursor.getColumnIndex(DB_FIELD_JSON));

                    cacheList.add(new Cache(id, url, json));

                    cursor.moveToNext();
                }

                cursor.close();
            }
        }

        closeDatabase();

        return cacheList;
    }

    private void sync(final Cache cache) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... params) {
                try {
                    return HttpManager.makeRequestURL(context, cache.getUrl(), HttpManager.RequestType.POST, new JSONObject(cache.getJson()), false);
                } catch (Exception e) {
                    return e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(Object object) {
                super.onPostExecute(object);

                if (object instanceof ResponseData) {
                    delete(cache.getId());
                }
            }
        }.execute();
    }
}