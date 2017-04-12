package br.com.allin.mobile.pushnotification.constants;

/**
 * Created by lucasrodrigues on 05/04/17.
 */

public class MessageConstants {
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
    public static final String DB_FIELD_READ = "read";
    public static final String DB_NAME = "allin_messages.db";

    public static String CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s " +
                    "(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s VARCHAR, %s VARCHAR, %s VARCHAR, %s VARCHAR, %s VARCHAR, " +
                    "%s VARCHAR, %s VARCHAR, %s VARCHAR, %s VARCHAR, %s INTEGER);",
                MessageConstants.TABLE_NAME, MessageConstants.DB_FIELD_ID,
                MessageConstants.DB_FIELD_ID_SEND, MessageConstants.DB_FIELD_SUBJECT, MessageConstants.DB_FIELD_DESCRIPTION,
                MessageConstants.DB_FIELD_ID_CAMPAIGN, MessageConstants.DB_FIELD_ID_LOGIN, MessageConstants.DB_FIELD_URL_SCHEME,
                MessageConstants.DB_FIELD_ACTION, MessageConstants.DB_FIELD_DATE_NOTIFICATION,
                MessageConstants.DB_FIELD_URL_TRANSACTIONAL, MessageConstants.DB_FIELD_READ);

    public static String SELECT = String.format("SELECT * FROM %s", MessageConstants.TABLE_NAME);
}
