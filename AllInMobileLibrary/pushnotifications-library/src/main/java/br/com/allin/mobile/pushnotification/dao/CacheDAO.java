package br.com.allin.mobile.pushnotification.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.allin.mobile.pushnotification.constants.CacheConstants;
import br.com.allin.mobile.pushnotification.entity.CacheEntity;

/**
 * Class that manages the CacheEntity database requests
 */
public class CacheDAO {
    private Context context;
    private SQLiteDatabase sqliteDatabase;

    public CacheDAO(Context context) {
        this.context = context;

        createTable();
    }

    private void openDatabase() {
        this.sqliteDatabase = context.openOrCreateDatabase(CacheConstants.DB_NAME, Context.MODE_PRIVATE, null);
    }

    private void closeDatabase() {
        if (sqliteDatabase != null) {
            sqliteDatabase.close();
        }

        sqliteDatabase = null;
    }

    private void createTable() {
        openDatabase();

        if (sqliteDatabase != null) {
            sqliteDatabase.execSQL(CacheConstants.CREATE_TABLE_CACHE);
        }

        closeDatabase();
    }

    /**
     * Inserts in the database URL and JSON request
     * (the ID is generated automatically by the local database)
     *
     * @param url URL attempt request
     * @param json JSON attempt request
    */
    public void insert(String url, String json) {
        openDatabase();

        if (sqliteDatabase != null) {
            sqliteDatabase.execSQL(CacheConstants.INSERT_CACHE
                    .replace("#VALUE1", url).replace("#VALUE2", json));
        }

        closeDatabase();
    }

    public void delete(int id) {
        openDatabase();

        if (sqliteDatabase != null) {
            sqliteDatabase.execSQL(CacheConstants.DELETE_CACHE.replace("#VALUE1", String.valueOf(id)));
        }

        closeDatabase();
    }

    public List<CacheEntity> getAll() {
        openDatabase();

        List<CacheEntity> cacheEntityList = new ArrayList<>();

        if (sqliteDatabase != null) {
            Cursor cursor = sqliteDatabase.rawQuery(CacheConstants.QUERY_CACHE, null);

            if (cursor != null) {
                cursor.moveToFirst();

                while (!cursor.isAfterLast()) {
                    int id = cursor.getInt(cursor.getColumnIndex(CacheConstants.DB_FIELD_ID));
                    String url = cursor.getString(cursor.getColumnIndex(CacheConstants.DB_FIELD_URL));
                    String json = cursor.getString(cursor.getColumnIndex(CacheConstants.DB_FIELD_JSON));

                    cacheEntityList.add(new CacheEntity(id, url, json));

                    cursor.moveToNext();
                }

                cursor.close();
            }
        }

        closeDatabase();

        return cacheEntityList;
    }
}