package br.com.allin.mobile.pushnotification.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.allin.mobile.pushnotification.constants.MessageConstant;
import br.com.allin.mobile.pushnotification.entity.allin.MessageEntity;

/**
 * Created by lucasrodrigues on 05/04/17.
 */

public class MessageDAO extends BaseDAO {
    public MessageDAO(Context context) {
        super(context, MessageConstant.DB_NAME, MessageConstant.CREATE_TABLE);
    }

    public long insert(MessageEntity message) {
        long idInsert = 0;

        openDatabase();

        if (sqliteDatabase != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MessageConstant.DB_FIELD_ID_SEND, message.getIdSend());
            contentValues.put(MessageConstant.DB_FIELD_DESCRIPTION, message.getDescription());
            contentValues.put(MessageConstant.DB_FIELD_SUBJECT, message.getSubject());
            contentValues.put(MessageConstant.DB_FIELD_ID_CAMPAIGN, message.getIdCampaign());
            contentValues.put(MessageConstant.DB_FIELD_ID_LOGIN, message.getIdLogin());
            contentValues.put(MessageConstant.DB_FIELD_URL_SCHEME, message.getUrlScheme());
            contentValues.put(MessageConstant.DB_FIELD_ACTION, message.getAction());
            contentValues.put(MessageConstant.DB_FIELD_DATE_NOTIFICATION, message.getDate());
            contentValues.put(MessageConstant.DB_FIELD_URL_TRANSACTIONAL, message.getUrlTransactional());
            contentValues.put(MessageConstant.DB_FIELD_URL_CAMPAIGN, message.getUrlCampaign());
            contentValues.put(MessageConstant.DB_FIELD_READ, message.isRead() ? 1 : 0);

            idInsert = sqliteDatabase.insert(MessageConstant.TABLE_NAME, null, contentValues);
        }

        closeDatabase();

        return idInsert;
    }

    public boolean delete(int id) {
        boolean success = false;

        openDatabase();

        if (sqliteDatabase != null) {
            success = sqliteDatabase
                    .delete(MessageConstant.TABLE_NAME, MessageConstant.DB_FIELD_ID + " = " + id, null) > 0;
        }

        closeDatabase();

        return success;
    }

    public boolean messageHasBeenRead(int id) {
        boolean success = false;

        openDatabase();

        if (sqliteDatabase != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MessageConstant.DB_FIELD_READ, 1);

            success = sqliteDatabase
                    .update(MessageConstant.TABLE_NAME, contentValues,
                            MessageConstant.DB_FIELD_ID + " = " + id, null) > 0;
        }

        closeDatabase();

        return success;
    }

    public List<MessageEntity> getAll() {
        openDatabase();

        List<MessageEntity> messageEntityList = new ArrayList<>();

        if (sqliteDatabase != null) {
            Cursor cursor = sqliteDatabase.rawQuery(MessageConstant.SELECT, null);

            if (cursor != null) {
                cursor.moveToFirst();

                while (!cursor.isAfterLast()) {
                    messageEntityList.add(new MessageEntity(cursor));

                    cursor.moveToNext();
                }

                cursor.close();
            }
        }

        closeDatabase();

        return messageEntityList;
    }
}
