package br.com.allin.mobile.pushnotification.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.allin.mobile.pushnotification.constants.CacheConstants;
import br.com.allin.mobile.pushnotification.entity.Cache;
import br.com.allin.mobile.pushnotification.http.HttpManager;
import br.com.allin.mobile.pushnotification.entity.ResponseData;

/**
 * Class that manages the Cache database requests
 */
public class CacheDAO {

    private CacheDAO() {
    }

    private Context context;
    private SQLiteDatabase sqliteDatabase;

    public void setContext(Context context) {
        this.context = context;
    }

    private static CacheDAO cache;

    /**
    * Creates (if there) and opens the connection to the database cache data
    *
    * @return Object instance
    */
    public static CacheDAO getInstance(Context context) {
        if (cache == null) {
            cache = new CacheDAO();
        }

        cache.setContext(context);
        cache.openDatabase();

        return cache;
    }

    private void openDatabase() {
        sqliteDatabase = context.openOrCreateDatabase(CacheConstants.DB_NAME, Context.MODE_PRIVATE, null);

        createTable();
    }

    private void closeDatabase() {
        if (sqliteDatabase != null) {
            sqliteDatabase.close();
        }
    }

    private void createTable() {
        if (sqliteDatabase != null) {
            sqliteDatabase.execSQL(CacheConstants.CREATE_TABLE_CACHE);
        }
    }

    /**
     * Inserts in the database URL and JSON request
     * (the ID is generated automatically by the local database)
     *
     * @param url URL attempt request
     * @param json JSON attempt request
    */
    public void insert(String url, String json) {
        if (sqliteDatabase != null) {
            sqliteDatabase.execSQL(CacheConstants.INSERT_CACHE
                    .replace("#VALUE1", url).replace("#VALUE2", json));
        }

        closeDatabase();
    }

    private void delete(int id) {
        if (sqliteDatabase != null) {
            sqliteDatabase.execSQL(CacheConstants.DELETE_CACHE.replace("#VALUE1", String.valueOf(id)));
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
            Cursor cursor = sqliteDatabase.rawQuery(CacheConstants.QUERY_CACHE, null);

            if (cursor != null) {
                while (!cursor.isAfterLast()) {
                    int id = cursor.getInt(cursor.getColumnIndex(CacheConstants.DB_FIELD_ID));
                    String url = cursor.getString(cursor.getColumnIndex(CacheConstants.DB_FIELD_URL));
                    String json = cursor.getString(cursor.getColumnIndex(CacheConstants.DB_FIELD_JSON));

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
                    return HttpManager.makeRequestURL(context, cache.getUrl(),
                            HttpManager.RequestType.POST, new JSONObject(cache.getJson()), false);
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