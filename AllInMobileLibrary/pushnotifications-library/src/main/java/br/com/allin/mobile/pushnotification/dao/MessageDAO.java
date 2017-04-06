package br.com.allin.mobile.pushnotification.dao;

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

    public void insert(MessageEntity messageEntity) {
        openDatabase();

        if (sqliteDatabase != null) {
            sqliteDatabase.execSQL(
                    Message.INSERT
                            .replace("#VALUE" + 0, messageEntity.getIdSend())
                            .replace("#VALUE" + 1, messageEntity.getSubject())
                            .replace("#VALUE" + 2, messageEntity.getDescription())
                            .replace("#VALUE" + 3, messageEntity.getIdCampaign())
                            .replace("#VALUE" + 4, messageEntity.getIdLogin())
                            .replace("#VALUE" + 5, messageEntity.getUrlScheme())
                            .replace("#VALUE" + 6, messageEntity.getAction())
                            .replace("#VALUE" + 7, messageEntity.getDate())
                            .replace("#VALUE" + 8, messageEntity.getUrlTransactional())
                            .replace("#VALUE" + 9, messageEntity.isRead() ? "1" : "0")
            );
        }

        closeDatabase();
    }

    public void delete(int id) {
        openDatabase();

        if (sqliteDatabase != null) {
            sqliteDatabase.execSQL(Message.DELETE.replace("#VALUE1", String.valueOf(id)));
        }

        closeDatabase();
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
