package br.com.allin.mobile.pushnotification.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.allin.mobile.pushnotification.constants.MessageDatabaseConstant;
import br.com.allin.mobile.pushnotification.entity.allin.AlMessage;

/**
 * Created by lucasrodrigues on 05/04/17.
 */

public class MessageDAO extends BaseDAO {
    public MessageDAO(Context context) {
        super(context, MessageDatabaseConstant.DB_NAME, MessageDatabaseConstant.CREATE_TABLE);
    }

    public long insert(AlMessage message) {
        long idInsert = 0;

        openDatabase();

        if (sqliteDatabase != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MessageDatabaseConstant.DB_FIELD_ID_SEND, message.getIdSend());
            contentValues.put(MessageDatabaseConstant.DB_FIELD_DESCRIPTION, message.getDescription());
            contentValues.put(MessageDatabaseConstant.DB_FIELD_SUBJECT, message.getSubject());
            contentValues.put(MessageDatabaseConstant.DB_FIELD_ID_CAMPAIGN, message.getIdCampaign());
            contentValues.put(MessageDatabaseConstant.DB_FIELD_ID_LOGIN, message.getIdLogin());
            contentValues.put(MessageDatabaseConstant.DB_FIELD_URL_SCHEME, message.getUrlScheme());
            contentValues.put(MessageDatabaseConstant.DB_FIELD_ACTION, message.getAction());
            contentValues.put(MessageDatabaseConstant.DB_FIELD_DATE_NOTIFICATION, message.getDate());
            contentValues.put(MessageDatabaseConstant.DB_FIELD_URL_TRANSACTIONAL, message.getUrlTransactional());
            contentValues.put(MessageDatabaseConstant.DB_FIELD_URL_CAMPAIGN, message.getUrlCampaign());
            contentValues.put(MessageDatabaseConstant.DB_FIELD_READ, message.isRead() ? 1 : 0);

            idInsert = sqliteDatabase.insert(MessageDatabaseConstant.TABLE_NAME, null, contentValues);
        }

        closeDatabase();

        return idInsert;
    }

    public boolean delete(long id) {
        boolean success = false;

        openDatabase();

        if (sqliteDatabase != null) {
            success = sqliteDatabase
                    .delete(MessageDatabaseConstant.TABLE_NAME,
                            MessageDatabaseConstant.DB_FIELD_ID + " = " + id, null) > 0;
        }

        closeDatabase();

        return success;
    }

    public boolean messageHasBeenRead(long id) {
        boolean success = false;

        openDatabase();

        if (sqliteDatabase != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MessageDatabaseConstant.DB_FIELD_READ, 1);

            success = sqliteDatabase
                    .update(MessageDatabaseConstant.TABLE_NAME, contentValues,
                            MessageDatabaseConstant.DB_FIELD_ID + " = " + id, null) > 0;
        }

        closeDatabase();

        return success;
    }

    public List<AlMessage> getAll() {
        openDatabase();

        List<AlMessage> alMessageList = new ArrayList<>();

        if (sqliteDatabase != null) {
            Cursor cursor = sqliteDatabase.rawQuery(MessageDatabaseConstant.SELECT, null);

            if (cursor != null) {
                cursor.moveToFirst();

                while (!cursor.isAfterLast()) {
                    alMessageList.add(new AlMessage(cursor));

                    cursor.moveToNext();
                }

                cursor.close();
            }
        }

        closeDatabase();

        return alMessageList;
    }
}
