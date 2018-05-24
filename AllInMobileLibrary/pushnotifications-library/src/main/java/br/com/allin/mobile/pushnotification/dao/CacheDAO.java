package br.com.allin.mobile.pushnotification.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.allin.mobile.pushnotification.constants.CacheDatabaseConstant;
import br.com.allin.mobile.pushnotification.entity.allin.AICache;

/**
 * Class that manages the AICache database requests
 */
public class CacheDAO extends BaseDAO {
    public CacheDAO(Context context) {
        super(context, CacheDatabaseConstant.DB_NAME, CacheDatabaseConstant.CREATE_TABLE);
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
            contentValues.put(CacheDatabaseConstant.DB_FIELD_URL, url);
            contentValues.put(CacheDatabaseConstant.DB_FIELD_JSON,json);

            idInsert = sqliteDatabase.insert(CacheDatabaseConstant.TABLE_NAME, null, contentValues);
        }

        closeDatabase();

        return idInsert;
    }

    public boolean delete(int id) {
        boolean success = false;

        openDatabase();

        if (sqliteDatabase != null) {
            success = sqliteDatabase.delete(
                    CacheDatabaseConstant.TABLE_NAME, CacheDatabaseConstant.DB_FIELD_ID + " = " + id, null) > 0;
        }

        closeDatabase();

        return success;
    }

    public List<AICache> getAll() {
        openDatabase();

        List<AICache> AICacheList = new ArrayList<>();

        if (sqliteDatabase != null) {
            Cursor cursor = sqliteDatabase.rawQuery(CacheDatabaseConstant.SELECT, null);

            if (cursor != null) {
                cursor.moveToFirst();

                while (!cursor.isAfterLast()) {
                    int id = cursor.getInt(cursor.getColumnIndex(CacheDatabaseConstant.DB_FIELD_ID));
                    String url = cursor.getString(cursor.getColumnIndex(CacheDatabaseConstant.DB_FIELD_URL));
                    String json = cursor.getString(cursor.getColumnIndex(CacheDatabaseConstant.DB_FIELD_JSON));

                    AICacheList.add(new AICache(id, url, json));

                    cursor.moveToNext();
                }

                cursor.close();
            }
        }

        closeDatabase();

        return AICacheList;
    }
}