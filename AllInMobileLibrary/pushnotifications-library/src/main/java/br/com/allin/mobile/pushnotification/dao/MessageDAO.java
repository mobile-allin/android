package br.com.allin.mobile.pushnotification.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.allin.mobile.pushnotification.constants.Message;
import br.com.allin.mobile.pushnotification.entity.MessageEntity;

/**
 * Created by lucasrodrigues on 05/04/17.
 */

public class MessageDAO extends BaseDAO {
    public MessageDAO(Context context) {
        super(context, Message.DB_NAME, Message.CREATE_TABLE);
    }

    public long insert(MessageEntity messageEntity) {
        long idInsert = 0;

        openDatabase();

        if (sqliteDatabase != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Message.DB_FIELD_ID_SEND, messageEntity.getIdSend());
            contentValues.put(Message.DB_FIELD_DESCRIPTION, messageEntity.getDescription());
            contentValues.put(Message.DB_FIELD_SUBJECT, messageEntity.getSubject());
            contentValues.put(Message.DB_FIELD_DESCRIPTION, messageEntity.getIdCampaign());
            contentValues.put(Message.DB_FIELD_ID_LOGIN, messageEntity.getIdLogin());
            contentValues.put(Message.DB_FIELD_URL_SCHEME, messageEntity.getUrlScheme());
            contentValues.put(Message.DB_FIELD_ACTION, messageEntity.getAction());
            contentValues.put(Message.DB_FIELD_DATE_NOTIFICATION, messageEntity.getDate());
            contentValues.put(Message.DB_FIELD_URL_TRANSACTIONAL, messageEntity.getUrlTransactional());
            contentValues.put(Message.DB_FIELD_READ, messageEntity.isRead() ? 1 : 0);

            idInsert = sqliteDatabase.insert(Message.TABLE_NAME, null, contentValues);
        }

        closeDatabase();

        return idInsert;
    }

    public boolean delete(int id) {
        boolean success = false;

        openDatabase();

        if (sqliteDatabase != null) {
            success = sqliteDatabase
                    .delete(Message.TABLE_NAME, Message.DB_FIELD_ID + " = " + id, null) > 0;
        }

        closeDatabase();

        return success;
    }

    public boolean messageHasBeenRead(int id) {
        boolean success = false;

        openDatabase();

        if (sqliteDatabase != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Message.DB_FIELD_READ, 1);

            success = sqliteDatabase
                    .update(Message.TABLE_NAME, contentValues,
                            Message.DB_FIELD_ID + " = " + id, null) > 0;
        }

        closeDatabase();

        return success;
    }

    public List<MessageEntity> getAll() {
        openDatabase();

        List<MessageEntity> messageEntityList = new ArrayList<>();

        if (sqliteDatabase != null) {
            Cursor cursor = sqliteDatabase.rawQuery(Message.SELECT, null);

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
