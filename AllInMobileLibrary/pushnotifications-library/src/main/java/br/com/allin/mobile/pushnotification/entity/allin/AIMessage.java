package br.com.allin.mobile.pushnotification.entity.allin;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Bundle;

import br.com.allin.mobile.pushnotification.identifiers.PushIdentifier;

/**
 * Created by lucasrodrigues on 05/04/17.
 */
@Entity(tableName = "message")
public class AIMessage {
    @PrimaryKey
    private int id;
    private String idSend;
    private String subject;
    private String description;
    private String idCampaign;
    private String idLogin;
    private String urlScheme;
    private String action;
    private String date;
    private boolean read;

    public AIMessage() {
    }

    @Ignore
    public AIMessage(Bundle bundle) {
        this.id = bundle.getInt(PushIdentifier.ID);
        this.idSend = bundle.getString(PushIdentifier.ID_SEND);
        this.subject = bundle.getString(PushIdentifier.TITLE);
        this.description = bundle.getString(PushIdentifier.BODY);
        this.idCampaign = bundle.getString(PushIdentifier.ID_CAMPAIGN);
        this.idLogin = bundle.getString(PushIdentifier.ID_LOGIN);
        this.urlScheme = bundle.getString(PushIdentifier.URL_SCHEME);
        this.action = bundle.getString(PushIdentifier.ACTION);
        this.date = bundle.getString(PushIdentifier.DATE);
        this.read = false;

        updateNullValues();
    }

    private void updateNullValues() {
        if (idSend == null) {
            idSend = "";
        }

        if (subject == null) {
            subject = "";
        }

        if (description == null) {
            description = "";
        }

        if (idCampaign == null) {
            idCampaign = "";
        }

        if (idLogin == null) {
            idLogin = "";
        }

        if (urlScheme == null) {
            urlScheme = "";
        }

        if (action == null) {
            action = "";
        }

        if (date == null) {
            date = "";
        }
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

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}