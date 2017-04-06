package br.com.allin.mobile.pushnotification.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by lucasrodrigues on 05/04/17.
 */

public abstract class BaseDAO {
    protected Context context;
    protected String createTable;
    protected String dbName;
    protected SQLiteDatabase sqliteDatabase;

    public BaseDAO(Context context, String dbName, String createTable) {
        this.context = context;
        this.createTable = createTable;
        this.dbName = dbName;

        createTable();
    }

    protected void openDatabase() {
        this.sqliteDatabase = context.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
    }

    protected void closeDatabase() {
        if (sqliteDatabase != null) {
            sqliteDatabase.close();
        }

        sqliteDatabase = null;
    }

    protected void createTable() {
        openDatabase();

        if (sqliteDatabase != null) {
            sqliteDatabase.execSQL(createTable);
        }

        closeDatabase();
    }
}
