package br.com.allin.mobile.pushnotification.constants;

/**
 * Created by lucasrodrigues on 05/04/17.
 */

public class Message {
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
                Message.TABLE_NAME, Message.DB_FIELD_ID,
                Message.DB_FIELD_ID_SEND, Message.DB_FIELD_SUBJECT, Message.DB_FIELD_DESCRIPTION,
                Message.DB_FIELD_ID_CAMPAIGN, Message.DB_FIELD_ID_LOGIN, Message.DB_FIELD_URL_SCHEME,
                Message.DB_FIELD_ACTION, Message.DB_FIELD_DATE_NOTIFICATION,
                Message.DB_FIELD_URL_TRANSACTIONAL, Message.DB_FIELD_READ);

    public static String INSERT = String.format(
                    "INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s) " +
                    "VALUES (" +
                        "'#VALUE0', '#VALUE1', '#VALUE2', '#VALUE3', '#VALUE4', " +
                        "'#VALUE5', '#VALUE6', '#VALUE7', '#VALUE8', #VALUE9" +
                    ");",
                    Message.TABLE_NAME, Message.DB_FIELD_ID_SEND, Message.DB_FIELD_SUBJECT,
                    Message.DB_FIELD_DESCRIPTION, Message.DB_FIELD_ID_CAMPAIGN,
                    Message.DB_FIELD_ID_LOGIN, Message.DB_FIELD_URL_SCHEME,
                    Message.DB_FIELD_ACTION, Message.DB_FIELD_DATE_NOTIFICATION,
                    Message.DB_FIELD_URL_TRANSACTIONAL, Message.DB_FIELD_READ);

    public static String SELECT = String.format("SELECT *FROM %s", Message.DB_FIELD_ID);

    public static String DELETE = String.format("DELETE " +
            "FROM %s WHERE %s = #VALUE1", Message.TABLE_NAME, Message.DB_FIELD_ID);
}
