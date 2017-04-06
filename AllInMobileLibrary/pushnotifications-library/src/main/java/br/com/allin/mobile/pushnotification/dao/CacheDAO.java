package br.com.allin.mobile.pushnotification.dao;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.allin.mobile.pushnotification.constants.Cache;
import br.com.allin.mobile.pushnotification.entity.CacheEntity;

/**
 * Class that manages the CacheEntity database requests
 */
public class CacheDAO extends BaseDAO {
    public CacheDAO(Context context) {
        super(context, Cache.DB_NAME, Cache.CREATE_TABLE);
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
            sqliteDatabase.execSQL(Cache.INSERT
                    .replace("#VALUE1", url).replace("#VALUE2", json));
        }

        closeDatabase();
    }

    public void delete(int id) {
        openDatabase();

        if (sqliteDatabase != null) {
            sqliteDatabase.execSQL(Cache.DELETE.replace("#VALUE1", String.valueOf(id)));
        }

        closeDatabase();
    }

    public List<CacheEntity> getAll() {
        openDatabase();

        List<CacheEntity> cacheEntityList = new ArrayList<>();

        if (sqliteDatabase != null) {
            Cursor cursor = sqliteDatabase.rawQuery(Cache.SELECT, null);

            if (cursor != null) {
                cursor.moveToFirst();

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
}