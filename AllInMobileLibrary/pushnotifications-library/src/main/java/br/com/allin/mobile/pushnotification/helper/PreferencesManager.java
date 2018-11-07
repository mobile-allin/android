package br.com.allin.mobile.pushnotification.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;

import br.com.allin.mobile.pushnotification.identifiers.PreferenceIdentifier;

/**
 * Manager preferences in the library.
 * Responsible for saving/edit the saved values in the device preferences.
 */
public class PreferencesManager {
    private final SharedPreferences preferences;

    /**
     * Default constructor.
     */
    public PreferencesManager(Context context) {
        this.preferences = context.getSharedPreferences(PreferenceIdentifier.PREFERENCES_ID, Context.MODE_PRIVATE);
    }

    /**
     * Write a value in the
     * <a href="http://developer.android.com/guide/topics/data/data-storage.html#pref" >SharedPreferences</a>.
     *
     * @param key Identifier value
     * @param value Value that will be recorder
     */
    public <T> void storeData(String key, T value) {
        SharedPreferences.Editor editor = preferences.edit();

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
     * @param key Identifier value
     * @param defaultValue Default value if the value is not found or NULL.
     *
     * @return Object with the return value (recovered from SharedPreferences)
     */
    @SuppressWarnings("unchecked")
    public <T> T getData(String key, Object defaultValue) {
        Object value;

        if (defaultValue instanceof Integer) {
            value = preferences.getInt(key, (Integer) defaultValue);
        } else if (defaultValue instanceof Float) {
            value =  preferences.getFloat(key, (Float) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            value =  preferences.getBoolean(key, (Boolean) defaultValue);
        } else {
            value =  preferences.getString(key, (String) defaultValue);
        }

        return (T) value;
    }

    /**
     * Clears all the settings saved in
     * <a href="http://developer.android.com/guide/topics/data/data-storage.html#pref">SharedPreferences</a>.
     */
    @SuppressWarnings("unused")
    public void clear() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
