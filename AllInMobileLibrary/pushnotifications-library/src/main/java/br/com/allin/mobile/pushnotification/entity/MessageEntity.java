package br.com.allin.mobile.pushnotification.entity;

import android.database.Cursor;
import android.os.Bundle;

import br.com.allin.mobile.pushnotification.constants.Message;

/**
 * Created by lucasrodrigues on 05/04/17.
 */

public class MessageEntity {
    private int id;
    private String idSend;
    private String subject;
    private String description;
    private String idCampaign;
    private String idLogin;
    private String urlScheme;
    private String action;
    private String date;
    private String urlTransactional;
    private boolean read;

    public MessageEntity(int id, String idSend, String subject, String description,
                         String idCampaign, String idLogin, String urlScheme,
                         String action, String date, String urlTransactional) {
        this.id = id;
        this.idSend = idSend;
        this.subject = subject;
        this.description = description;
        this.idCampaign = idCampaign;
        this.idLogin = idLogin;
        this.urlScheme = urlScheme;
        this.action = action;
        this.date = date;
        this.urlTransactional = urlTransactional;
    }

    public MessageEntity(int id, String idSend, String subject, String description,
                         String idCampaign, String idLogin, String urlScheme,
                         String action, String date, String urlTransactional, boolean read) {
        this.id = id;
        this.idSend = idSend;
        this.subject = subject;
        this.description = description;
        this.idCampaign = idCampaign;
        this.idLogin = idLogin;
        this.urlScheme = urlScheme;
        this.action = action;
        this.date = date;
        this.urlTransactional = urlTransactional;
        this.read = read;
    }

    public MessageEntity(Cursor cursor) {
        this.id = cursor.getInt(
                cursor.getColumnIndex(Message.DB_FIELD_ID));
        this.idSend = cursor.getString(
                cursor.getColumnIndex(Message.DB_FIELD_ID_SEND));
        this.subject = cursor.getString(
                cursor.getColumnIndex(Message.DB_FIELD_SUBJECT));
        this.description = cursor.getString(
                cursor.getColumnIndex(Message.DB_FIELD_DESCRIPTION));
        this.idCampaign = cursor.getString(
                cursor.getColumnIndex(Message.DB_FIELD_ID_CAMPAIGN));
        this.idLogin = cursor.getString(
                cursor.getColumnIndex(Message.DB_FIELD_ID_LOGIN));
        this.urlScheme = cursor.getString(
                cursor.getColumnIndex(Message.DB_FIELD_URL_SCHEME));
        this.action = cursor.getString(
                cursor.getColumnIndex(Message.DB_FIELD_ACTION));
        this.date = cursor.getString(
                cursor.getColumnIndex(Message.DB_FIELD_DATE_NOTIFICATION));
        this.urlTransactional = cursor.getString(
                cursor.getColumnIndex(Message.DB_FIELD_URL_TRANSACTIONAL));
        this.read = cursor.getInt(
                cursor.getColumnIndex(Message.DB_FIELD_READ)) == 1;
    }

    public MessageEntity(Bundle bundle) {
        this.id = bundle.getInt(Message.DB_FIELD_ID);
        this.idSend = bundle.getString(Message.DB_FIELD_ID_SEND);
        this.subject = bundle.getString(Message.DB_FIELD_SUBJECT);
        this.description = bundle.getString(Message.DB_FIELD_DESCRIPTION);
        this.idCampaign = bundle.getString(Message.DB_FIELD_ID_CAMPAIGN);
        this.idLogin = bundle.getString(Message.DB_FIELD_ID_LOGIN);
        this.urlScheme = bundle.getString(Message.DB_FIELD_URL_SCHEME);
        this.action = bundle.getString(Message.DB_FIELD_ACTION);
        this.date = bundle.getString(Message.DB_FIELD_DATE_NOTIFICATION);
        this.urlTransactional = bundle.getString(Message.DB_FIELD_URL_TRANSACTIONAL);
        this.read = bundle.getInt(Message.DB_FIELD_READ) == 1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdSend() {
        return idSend;
    }

    public void setIdSend(String idSend) {
        this.idSend = idSend;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdCampaign() {
        return idCampaign;
    }

    public void setIdCampaign(String idCampaign) {
        this.idCampaign = idCampaign;
    }

    public String getIdLogin() {
        return idLogin;
    }

    public void setIdLogin(String idLogin) {
        this.idLogin = idLogin;
    }

    public String getUrlScheme() {
        return urlScheme;
    }

    public void setUrlScheme(String urlScheme) {
        this.urlScheme = urlScheme;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrlTransactional() {
        return urlTransactional;
    }

    public void setUrlTransactional(String urlTransactional) {
        this.urlTransactional = urlTransactional;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}