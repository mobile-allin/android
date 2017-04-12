package br.com.allin.mobile.pushnotification.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.allin.mobile.pushnotification.constants.MessageConstants;
import br.com.allin.mobile.pushnotification.entity.MessageEntity;

/**
 * Created by lucasrodrigues on 05/04/17.
 */

public class MessageDAO extends BaseDAO {
    public MessageDAO(Context context) {
        super(context, MessageConstants.DB_NAME, MessageConstants.CREATE_TABLE);
    }

    public long insert(MessageEntity messageEntity) {
        long idInsert = 0;

        openDatabase();

        if (sqliteDatabase != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MessageConstants.DB_FIELD_ID_SEND, messageEntity.getIdSend());
            contentValues.put(MessageConstants.DB_FIELD_DESCRIPTION, messageEntity.getDescription());
            contentValues.put(MessageConstants.DB_FIELD_SUBJECT, messageEntity.getSubject());
            contentValues.put(MessageConstants.DB_FIELD_DESCRIPTION, messageEntity.getIdCampaign());
            contentValues.put(MessageConstants.DB_FIELD_ID_LOGIN, messageEntity.getIdLogin());
            contentValues.put(MessageConstants.DB_FIELD_URL_SCHEME, messageEntity.getUrlScheme());
            contentValues.put(MessageConstants.DB_FIELD_ACTION, messageEntity.getAction());
            contentValues.put(MessageConstants.DB_FIELD_DATE_NOTIFICATION, messageEntity.getDate());
            contentValues.put(MessageConstants.DB_FIELD_URL_TRANSACTIONAL, messageEntity.getUrlTransactional());
            contentValues.put(MessageConstants.DB_FIELD_READ, messageEntity.isRead() ? 1 : 0);

            idInsert = sqliteDatabase.insert(MessageConstants.TABLE_NAME, null, contentValues);
        }

        closeDatabase();

        return idInsert;
    }

    public boolean delete(int id) {
        boolean success = false;

        openDatabase();

        if (sqliteDatabase != null) {
            success = sqliteDatabase
                    .delete(MessageConstants.TABLE_NAME, MessageConstants.DB_FIELD_ID + " = " + id, null) > 0;
        }

        closeDatabase();

        return success;
    }

    public boolean messageHasBeenRead(int id) {
        boolean success = false;

        openDatabase();

        if (sqliteDatabase != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MessageConstants.DB_FIELD_READ, 1);

            success = sqliteDatabase
                    .update(MessageConstants.TABLE_NAME, contentValues,
                            MessageConstants.DB_FIELD_ID + " = " + id, null) > 0;
        }

        closeDatabase();

        return success;
    }

    public List<MessageEntity> getAll() {
        openDatabase();

        List<MessageEntity> messageEntityList = new ArrayList<>();

        if (sqliteDatabase != null) {
            Cursor cursor = sqliteDatabase.rawQuery(MessageConstants.SELECT, null);

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
