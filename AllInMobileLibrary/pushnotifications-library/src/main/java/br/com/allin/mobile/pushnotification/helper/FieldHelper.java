package br.com.allin.mobile.pushnotification.helper;

import java.lang.reflect.Field;

/**
 * Created by lucasrodrigues on 17/01/18.
 */

public class FieldHelper {
    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);

            return idField.getInt(idField);
        } catch (Exception e) {
            return -1;
        }
    }
}
