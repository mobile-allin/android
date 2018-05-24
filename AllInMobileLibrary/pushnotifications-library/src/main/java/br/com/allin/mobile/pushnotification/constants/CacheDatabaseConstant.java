package br.com.allin.mobile.pushnotification.constants;

public interface CacheDatabaseConstant {
    String TABLE_NAME = "cache";
    String DB_FIELD_ID = "id";
    String DB_FIELD_URL = "url";
    String DB_FIELD_JSON = "json";
    String DB_NAME = "allin.db";

    String CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s " +
                    "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s VARCHAR, %s VARCHAR);",
                    CacheDatabaseConstant.TABLE_NAME, CacheDatabaseConstant.DB_FIELD_ID,
                    CacheDatabaseConstant.DB_FIELD_URL, CacheDatabaseConstant.DB_FIELD_JSON);

    String SELECT = String.format("SELECT %s, %s, %s " +
                    "FROM %s", CacheDatabaseConstant.DB_FIELD_ID, CacheDatabaseConstant.DB_FIELD_URL,
                    CacheDatabaseConstant.DB_FIELD_JSON, CacheDatabaseConstant.TABLE_NAME);
}
