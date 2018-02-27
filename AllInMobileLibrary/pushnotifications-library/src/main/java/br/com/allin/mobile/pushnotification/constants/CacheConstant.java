package br.com.allin.mobile.pushnotification.constants;

public interface CacheConstant {
    String TABLE_NAME = "cache";
    String DB_FIELD_ID = "id";
    String DB_FIELD_URL = "url";
    String DB_FIELD_JSON = "json";
    String DB_NAME = "allin.db";

    String CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s " +
                    "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s VARCHAR, %s VARCHAR);",
                    CacheConstant.TABLE_NAME, CacheConstant.DB_FIELD_ID,
                    CacheConstant.DB_FIELD_URL, CacheConstant.DB_FIELD_JSON);

    String SELECT = String.format("SELECT %s, %s, %s " +
                    "FROM %s", CacheConstant.DB_FIELD_ID, CacheConstant.DB_FIELD_URL,
                    CacheConstant.DB_FIELD_JSON, CacheConstant.TABLE_NAME);
}
