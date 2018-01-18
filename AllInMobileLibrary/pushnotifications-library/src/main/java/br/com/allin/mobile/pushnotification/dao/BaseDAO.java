package br.com.allin.mobile.pushnotification.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by lucasrodrigues on 05/04/17.
 */

public abstract class BaseDAO {
    private Context context;
    private String createTable;
    private String dbName;

    SQLiteDatabase sqliteDatabase;

    BaseDAO(Context context, String dbName, String createTable) {
        this.context = context;
        this.createTable = createTable;
        this.dbName = dbName;

        createTable();
    }

    void openDatabase() {
        if (context != null) {
            sqliteDatabase = context.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
        }
    }

    void closeDatabase() {
        if (sqliteDatabase != null) {
            sqliteDatabase.close();
        }

        sqliteDatabase = null;
    }

    void createTable() {
        openDatabase();

        if (sqliteDatabase != null) {
            sqliteDatabase.execSQL(createTable);
        }

        closeDatabase();
    }
}
