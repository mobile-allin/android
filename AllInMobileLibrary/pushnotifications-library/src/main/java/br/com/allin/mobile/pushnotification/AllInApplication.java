package br.com.allin.mobile.pushnotification;

import android.app.Application;

/**
 * Created by lucasrodrigues on 7/12/16.
 */
public abstract class AllInApplication extends Application {
    public abstract void onActionNotification(String action);
}
