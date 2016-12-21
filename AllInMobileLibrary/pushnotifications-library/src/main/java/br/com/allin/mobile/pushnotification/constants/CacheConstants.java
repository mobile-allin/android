package br.com.allin.mobile.pushnotification.constants;

public class CacheConstants {
    public static String TABLE_NAME = "cache";
    public static String DB_FIELD_ID = "id";
    public static String DB_FIELD_URL = "url";
    public static String DB_FIELD_JSON = "json";
    public static String DB_NAME = "allin.db";

    public static String CREATE_TABLE_CACHE = String.format("CREATE TABLE IF NOT EXISTS %s " +
                    "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s VARCHAR, %s VARCHAR);",
            CacheConstants.TABLE_NAME, CacheConstants.DB_FIELD_ID,
            CacheConstants.DB_FIELD_URL, CacheConstants.DB_FIELD_JSON);
    public static String INSERT_CACHE = String.format("INSERT INTO %s (%s, %s) " +
                    "VALUES ('#VALUE1', '#VALUE2');", CacheConstants.TABLE_NAME,
            CacheConstants.DB_FIELD_URL, CacheConstants.DB_FIELD_JSON);
    public static String QUERY_CACHE = String.format("SELECT %s, %s, %s " +
                    "FROM %s", CacheConstants.DB_FIELD_ID, CacheConstants.DB_FIELD_URL,
            CacheConstants.DB_FIELD_JSON, CacheConstants.TABLE_NAME);
    public static String DELETE_CACHE = String.format("DELETE " +
            "FROM %s WHERE %s = #VALUE1", CacheConstants.TABLE_NAME, CacheConstants.DB_FIELD_ID);
}
