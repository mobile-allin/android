package br.com.allin.mobile.pushnotification.helper;

import android.content.Context;

import br.com.allin.mobile.pushnotification.AlliNPush;

/**
 * Created by lucasrodrigues on 17/01/18.
 */

public class FieldHelper {
    public static int getResId(String resName, String type) {

        try {
            Context context = AlliNPush.getInstance().getContext();

            return context.getResources().getIdentifier(resName, type, context.getPackageName());
        } catch (Exception e) {
            return -1;
        }
    }
}
