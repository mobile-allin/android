package br.com.allin.mobile.pushnotification.dao;

import android.content.ContentValues;
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
    public long insert(String url, String json) {
        long idInsert = 0;

        openDatabase();

        if (sqliteDatabase != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Cache.DB_FIELD_URL, url);
            contentValues.put(Cache.DB_FIELD_JSON,json);

            idInsert = sqliteDatabase.insert(Cache.TABLE_NAME, null, contentValues);
        }

        closeDatabase();

        return idInsert;
    }

    public boolean delete(int id) {
        boolean success = false;

        openDatabase();

        if (sqliteDatabase != null) {
            success = sqliteDatabase
                    .delete(Cache.TABLE_NAME, Cache.DB_FIELD_ID + " = " + id, null) > 0;
        }

        closeDatabase();

        return success;
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