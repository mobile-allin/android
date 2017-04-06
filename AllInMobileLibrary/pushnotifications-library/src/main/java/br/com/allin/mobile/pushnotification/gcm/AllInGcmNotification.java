package br.com.allin.mobile.pushnotification.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.net.URLDecoder;

import br.com.allin.mobile.pushnotification.AllInPush;
import br.com.allin.mobile.pushnotification.SharedPreferencesManager;
import br.com.allin.mobile.pushnotification.Util;
import br.com.allin.mobile.pushnotification.constants.Notification;
import br.com.allin.mobile.pushnotification.constants.Preferences;
import br.com.allin.mobile.pushnotification.entity.MessageEntity;

/**
 * Class that provides the notification of receipt of a push GCM.
 */
public class AllInGcmNotification {
    private AllInGcmNotification() {
    }

    /**
     * Create a standard notification with title and text, sending additional parameters from a @code {Bundle}.
     *
     * @param title Notification title
     * @param content Content (text) notification
     * @param extras Parameters to be included in the notification.
     */
    public static void showNotification(Context context, String title, String content, Bundle extras) {
        if (content == null || extras == null) {
            return;
        }

        AllInPush.getInstance().addMessage(new MessageEntity(extras));

        NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder(context);

        String scheme = extras.getString(Notification.URL_SCHEME);

        if (scheme != null && scheme.trim().length() > 0) {
            try {
                scheme = URLDecoder.decode(scheme, "UTF-8");
            } catch (Exception e) {
                Log.e(AllInGcmNotification.class.toString(), "ERRO IN DECODE URL");
            } finally {
                if (scheme.contains("##id_push##")) {
                    scheme = scheme.replace("##id_push##", Util.md5(AllInPush.getInstance().getDeviceId()));
                }

                extras.putString(Notification.URL_SCHEME, scheme);
            }
        }

        Intent intent = new Intent(BroadcastNotification.BROADCAST_NOTIFICATION);
        intent.putExtras(extras);
        intent.putExtra(Notification.SUBJECT, title);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(context);
        String backgroundColor = sharedPreferencesManager
                .getData(Preferences.KEY_BACKGROUND_NOTIFICATION, null);

        int whiteIcon = sharedPreferencesManager
                .getData(Preferences.KEY_WHITE_ICON_NOTIFICATION, 0);

        int icon = sharedPreferencesManager
                .getData(Preferences.KEY_ICON_NOTIFICATION, 0);

        if (icon == 0) {
            notificationCompatBuilder
                    .setSmallIcon(whiteIcon != 0 ? whiteIcon : getNotificationIcon(context));
        } else {
            notificationCompatBuilder
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), icon))
                    .setSmallIcon(whiteIcon);
        }

        notificationCompatBuilder
                .setColor(backgroundColor != null ?
                        Color.parseColor(backgroundColor) : Color.TRANSPARENT)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS
                        | NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setGroupSummary(true)
                .setGroup("messages")
                .setContentIntent(pendingIntent)
                .setContentText(content)
                .setContentTitle(title)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Integer.MAX_VALUE, notificationCompatBuilder.build());
    }

    /**
     * Returns the id of the application of the default icon.
     *
     * @param context Application context.
     *
     * @return Application icon id.
     */
    private static int getNotificationIcon(Context context) {
        String packageName = context.getApplicationContext().getPackageName();
        PackageManager packageManager = context.getPackageManager();

        int iconResource = 0;

        try {
            ApplicationInfo applicationInfo = packageManager
                    .getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            iconResource = applicationInfo.icon;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return iconResource;

    }

}
