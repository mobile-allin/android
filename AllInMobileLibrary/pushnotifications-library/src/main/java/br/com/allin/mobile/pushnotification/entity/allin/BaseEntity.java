package br.com.allin.mobile.pushnotification.entity.allin;

/**
 * Created by lucasrodrigues on 07/02/18.
 */

public class BaseEntity {
    private String key;
    private String value;

    public BaseEntity(String key, String value) {
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
}
