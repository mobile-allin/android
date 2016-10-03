package br.com.allin.mobile.pushnotification.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.allin.mobile.pushnotification.constants.Cache;
import br.com.allin.mobile.pushnotification.entity.CacheEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.http.HttpManager;
import br.com.allin.mobile.pushnotification.entity.ResponseEntity;

/**
 * Class that manages the CacheEntity database requests
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
        sqliteDatabase = context.openOrCreateDatabase(Cache.DB_NAME, Context.MODE_PRIVATE, null);

        createTable();
    }

    private void closeDatabase() {
        if (sqliteDatabase != null) {
            sqliteDatabase.close();
        }
    }

    private void createTable() {
        if (sqliteDatabase != null) {
            sqliteDatabase.execSQL(Cache.CREATE_TABLE_CACHE);
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
            sqliteDatabase.execSQL(Cache.INSERT_CACHE
                    .replace("#VALUE1", url).replace("#VALUE2", json));
        }

        closeDatabase();
    }

    private void delete(int id) {
        if (sqliteDatabase != null) {
            sqliteDatabase.execSQL(Cache.DELETE_CACHE.replace("#VALUE1", String.valueOf(id)));
        }

        closeDatabase();
    }

    /**
     * Search all requests not made yet and performs according to the recorded information
    */
    public  void sync() {
        List<CacheEntity> cacheList = getAll();

        for (CacheEntity cacheEntity : cacheList) {
            sync(cacheEntity);
        }
    }

    private List<CacheEntity> getAll() {
        List<CacheEntity> cacheEntityList = new ArrayList<>();

        if (sqliteDatabase != null) {
            Cursor cursor = sqliteDatabase.rawQuery(Cache.QUERY_CACHE, null);

            if (cursor != null) {
                while (!cursor.isAfterLast()) {
                    int id = cursor.getInt(cursor.getColumnIndex(Cache.DB_FIELD_ID));
                    String url = cursor.getString(cursor.getColumnIndex(Cache.DB_FIELD_URL));
                    String json = cursor.getString(cursor.getColumnIndex(Cache.DB_FIELD_JSON));

                    cacheEntityList.add(new CacheEntity(id, url, json));

                    cursor.moveToNext();
                }

                cursor.close();
            }
        }

        closeDatabase();

        return cacheEntityList;
    }

    private void sync(final CacheEntity cacheEntity) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... params) {
                try {
                    return HttpManager.makeRequestURL(context, cacheEntity.getUrl(),
                            RequestType.POST, new JSONObject(cacheEntity.getJson()), false);
                } catch (Exception e) {
                    return e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(Object object) {
                super.onPostExecute(object);

                if (object instanceof ResponseEntity) {
                    delete(cacheEntity.getId());
                }
            }
        }.execute();
    }
}