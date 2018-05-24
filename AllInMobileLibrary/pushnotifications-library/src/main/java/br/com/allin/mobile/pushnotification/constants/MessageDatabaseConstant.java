package br.com.allin.mobile.pushnotification.constants;

/**
 * Created by lucasrodrigues on 05/04/17.
 */

public interface MessageDatabaseConstant {
    String TABLE_NAME = "messages";
    String DB_FIELD_ID = "id";
    String DB_FIELD_ID_SEND = "id_send";
    String DB_FIELD_SUBJECT = "subject";
    String DB_FIELD_DESCRIPTION = "description";
    String DB_FIELD_ID_CAMPAIGN = "id_campaign";
    String DB_FIELD_ID_LOGIN = "id_login";
    String DB_FIELD_URL_SCHEME = "url_scheme";
    String DB_FIELD_ACTION = "action";
    String DB_FIELD_DATE_NOTIFICATION = "date";
    String DB_FIELD_URL_TRANSACTIONAL = "url_transactional";
    String DB_FIELD_URL_CAMPAIGN = "url_campaign";
    String DB_FIELD_READ = "read";
    String DB_NAME = "allin_messages.db";

    String CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s " +
                "(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s VARCHAR, %s VARCHAR, %s VARCHAR, %s VARCHAR, %s VARCHAR, " +
                "%s VARCHAR, %s VARCHAR, %s VARCHAR, %s VARCHAR, %s VARCHAR, %s INTEGER);",
                MessageDatabaseConstant.TABLE_NAME,
                MessageDatabaseConstant.DB_FIELD_ID, MessageDatabaseConstant.DB_FIELD_ID_SEND,
                MessageDatabaseConstant.DB_FIELD_SUBJECT, MessageDatabaseConstant.DB_FIELD_DESCRIPTION,
                MessageDatabaseConstant.DB_FIELD_ID_CAMPAIGN, MessageDatabaseConstant.DB_FIELD_ID_LOGIN,
                MessageDatabaseConstant.DB_FIELD_URL_SCHEME, MessageDatabaseConstant.DB_FIELD_ACTION,
                MessageDatabaseConstant.DB_FIELD_DATE_NOTIFICATION, MessageDatabaseConstant.DB_FIELD_URL_TRANSACTIONAL,
                MessageDatabaseConstant.DB_FIELD_URL_CAMPAIGN, MessageDatabaseConstant.DB_FIELD_READ);

    String SELECT = String.format("SELECT * FROM %s", MessageDatabaseConstant.TABLE_NAME);
}
