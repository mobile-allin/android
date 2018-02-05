package br.com.allin.mobile.pushnotification.constants;

/**
 * Created by lucasrodrigues on 05/04/17.
 */

public class MessageConstant {
    public static final String TABLE_NAME = "messages";
    public static final String DB_FIELD_ID = "id";
    public static final String DB_FIELD_ID_SEND = "id_send";
    public static final String DB_FIELD_SUBJECT = "subject";
    public static final String DB_FIELD_DESCRIPTION = "description";
    public static final String DB_FIELD_ID_CAMPAIGN = "id_campaign";
    public static final String DB_FIELD_ID_LOGIN = "id_login";
    public static final String DB_FIELD_URL_SCHEME = "url_scheme";
    public static final String DB_FIELD_ACTION = "action";
    public static final String DB_FIELD_DATE_NOTIFICATION = "date";
    public static final String DB_FIELD_URL_TRANSACTIONAL = "url_transactional";
    public static final String DB_FIELD_URL_CAMPAIGN = "url_campaign";
    public static final String DB_FIELD_READ = "read";
    public static final String DB_NAME = "allin_messages.db";

    public static String CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s " +
                "(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s VARCHAR, %s VARCHAR, %s VARCHAR, %s VARCHAR, %s VARCHAR, " +
                "%s VARCHAR, %s VARCHAR, %s VARCHAR, %s VARCHAR, %s VARCHAR, %s INTEGER);",
                MessageConstant.TABLE_NAME,
                MessageConstant.DB_FIELD_ID, MessageConstant.DB_FIELD_ID_SEND,
                MessageConstant.DB_FIELD_SUBJECT, MessageConstant.DB_FIELD_DESCRIPTION,
                MessageConstant.DB_FIELD_ID_CAMPAIGN, MessageConstant.DB_FIELD_ID_LOGIN,
                MessageConstant.DB_FIELD_URL_SCHEME, MessageConstant.DB_FIELD_ACTION,
                MessageConstant.DB_FIELD_DATE_NOTIFICATION, MessageConstant.DB_FIELD_URL_TRANSACTIONAL,
                MessageConstant.DB_FIELD_URL_CAMPAIGN, MessageConstant.DB_FIELD_READ);

    public static String SELECT = String.format("SELECT * FROM %s", MessageConstant.TABLE_NAME);
}
