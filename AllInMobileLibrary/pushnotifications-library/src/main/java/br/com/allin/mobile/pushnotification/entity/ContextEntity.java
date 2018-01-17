package br.com.allin.mobile.pushnotification.entity;

import android.content.Context;

/**
 * Created by lucasrodrigues on 17/01/18.
 */

public class ContextEntity {
    private Context context;

    public ContextEntity(Context context) {
        this.context = context;
    }

    public Context getApplicationContext() {
        return context;
    }
}
