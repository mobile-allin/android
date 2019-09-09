package br.com.allin.mobile.pushnotification.helper;

import android.content.Context;
import android.content.SharedPreferences;

import br.com.allin.mobile.pushnotification.identifiers.PreferenceIdentifier;

/**
 * Manager preferences in the library.
 * Responsible for saving/edit the saved values in the device preferences.
 */
public class PreferencesManager {
    private SharedPreferences preferences = null;

    /**
     * Default constructor.
     */
    public PreferencesManager(Context context) {
        if (context == null) {
            return;
        }

        this.preferences = context.getSharedPreferences(PreferenceIdentifier.PREFERENCES_ID, Context.MODE_PRIVATE);
    }

    /**
     * Write a value in the
     * <a href="http://developer.android.com/guide/topics/data/data-storage.html#pref" >SharedPreferences</a>.
     *
     * @param key   Identifier value
     * @param value Value that will be recorder
     */
    public <T> void storeData(String key, T value) {
        if (this.preferences == null) {
            return;
        }

        SharedPreferences.Editor editor = this.preferences.edit();

        if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        }

        editor.apply();
    }

    /**
     * Returns a value recorded in
     * <a href="http://developer.android.com/guide/topics/data/data-storage.html#pref">SharedPreferences</a>.
     *
     * @param key          Identifier value
     * @param defaultValue Default value if the value is not found or NULL.
     * @return Object with the return value (recovered from SharedPreferences)
     */
    @SuppressWarnings("unchecked")
    public <T> T getData(String key, Object defaultValue) {
        if (this.preferences == null) {
            return null;
        }

        Object value;

        if (defaultValue instanceof Integer) {
            value = this.preferences.getInt(key, (Integer) defaultValue);
        } else if (defaultValue instanceof Float) {
            value = this.preferences.getFloat(key, (Float) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            value = this.preferences.getBoolean(key, (Boolean) defaultValue);
        } else {
            value = this.preferences.getString(key, (String) defaultValue);
        }

        return (T) value;
    }

    /**
     * Clears all the settings saved in
     * <a href="http://developer.android.com/guide/topics/data/data-storage.html#pref">SharedPreferences</a>.
     */
    @SuppressWarnings("unused")
    public void clear() {
        if (this.preferences == null) {
            return;
        }

        SharedPreferences.Editor editor = this.preferences.edit();
        editor.clear();
        editor.apply();
    }
}
