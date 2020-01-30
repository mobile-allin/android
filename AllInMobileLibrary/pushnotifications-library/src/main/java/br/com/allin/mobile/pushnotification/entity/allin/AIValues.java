package br.com.allin.mobile.pushnotification.entity.allin;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lucasrodrigues on 07/02/18.
 */

public class AIValues {
    private String key;
    private String value;

    public AIValues(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @NonNull
    @Override
    public String toString() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("key", this.getKey());
            jsonObject.put("value", this.getValue());

            return jsonObject.toString();
        } catch (JSONException e) {
            return super.toString();
        }
    }

    public String toJSON() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("key", this.getKey());
            jsonObject.put("value", this.getValue());

            return jsonObject.toString();
        } catch (JSONException e) {
            return "";
        }
    }
}
