package br.com.allin.mobile.pushnotification.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.support.v7.app.NotificationCompat.Builder;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.URLDecoder;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.constants.BroadcastNotificationConstant;
import br.com.allin.mobile.pushnotification.helper.PreferencesManager;
import br.com.allin.mobile.pushnotification.helper.Util;
import br.com.allin.mobile.pushnotification.constants.ActionConstant;
import br.com.allin.mobile.pushnotification.constants.NotificationConstant;
import br.com.allin.mobile.pushnotification.constants.PreferencesConstant;
import br.com.allin.mobile.pushnotification.entity.allin.AlMessage;
import br.com.allin.mobile.pushnotification.http.DownloadImage;

/**
 * Class that provides the notification of receipt of a push GCM.
 */
public class AllInGcmNotification {
    private WeakReference<Context> contextWeakReference;
    private PreferencesManager preferencesManager;

    public AllInGcmNotification(Context context) {
        this.preferencesManager = new PreferencesManager(context);
        this.contextWeakReference = new WeakReference<>(context);
    }

    /**
     * Create a standard notification with title and text, sending additional parameters from a @code {Bundle}.
     *
     * @param extras ParametersConstant to be included in the notification.
     */
    void showNotification(final Bundle extras) {
        if (extras == null) {
            return;
        }

        dealScheme(extras);

        String image = extras.getString(NotificationConstant.IMAGE);

        if (Util.isNullOrClear(image)) {
            showNotification(null, extras);
        } else {
            new DownloadImage(image, new DownloadImage.OnDownloadCompleted() {
                @Override
                public void onCompleted(Bitmap bitmap) {
                    showNotification(bitmap, extras);
                }

                @Override
                public void onError() {
                    showNotification(null, extras);
                }
            }).execute();
        }
    }

    private void dealScheme(Bundle extras) {
        String scheme = extras.getString(NotificationConstant.URL_SCHEME);

        if (scheme != null && scheme.trim().length() > 0) {
            try {
                scheme = URLDecoder.decode(scheme, "UTF-8");
            } catch (Exception e) {
                Log.e(AllInGcmNotification.class.toString(), "ERRO IN DECODE URL");
            } finally {
                if (scheme.contains("##id_push##")) {
                    scheme = scheme.replace("##id_push##",
                            Util.md5(AlliNPush.getInstance().getDeviceToken()));
                }

                extras.putString(NotificationConstant.URL_SCHEME, scheme);
            }
        }
    }

    private void showNotification(Bitmap bitmap, Bundle extras) {
        String title = extras.getString(NotificationConstant.SUBJECT);
        String content = extras.getString(NotificationConstant.DESCRIPTION);

        if (title == null || title.trim().length() == 0 || content == null || content.trim().length() == 0) {
            return;
        }

        Context context = contextWeakReference.get();
        int color = preferencesManager.getData(PreferencesConstant.BACKGROUND_NOTIFICATION, 0);
        int whiteIcon = preferencesManager.getData(PreferencesConstant.WHITE_ICON_NOTIFICATION, 0);
        int icon = preferencesManager.getData(PreferencesConstant.ICON_NOTIFICATION, 0);
        long idMessage = AlliNPush.getInstance().addMessage(new AlMessage(extras));

        extras.putLong(NotificationConstant.ID, idMessage);

        Intent intent = new Intent();
        intent.setAction(BroadcastNotificationConstant.ACTION);
        intent.putExtras(extras);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Builder notificationCompatBuilder = new Builder(context);

        if (icon == 0) {
            notificationCompatBuilder
                .setSmallIcon(whiteIcon != 0 ? whiteIcon : getNotificationIcon(context));
        } else {
            notificationCompatBuilder
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), icon))
                .setSmallIcon(whiteIcon);
        }

        notificationCompatBuilder
            .setColor(color == 0 ? Color.TRANSPARENT : ContextCompat.getColor(context, color))
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setGroupSummary(true)
            .setGroup("messages")
            .setContentIntent(pendingIntent)
            .setContentText(content)
            .setContentTitle(title)
            .setAutoCancel(true);

        if (bitmap != null) {
            notificationCompatBuilder.setStyle(
                new NotificationCompat.BigPictureStyle().bigPicture(bitmap)
            );
        } else {
            notificationCompatBuilder.setStyle(
                new NotificationCompat.BigTextStyle().bigText(content)
            );
        }

        addActions(context, notificationCompatBuilder, extras);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify((int) idMessage, notificationCompatBuilder.build());
        }
    }

    private void addActions(Context context, Builder notificationBuilder, Bundle extras) {
        String actions = extras.getString(NotificationConstant.ACTIONS);

        if (actions != null && !TextUtils.isEmpty(actions)) {
            try {
                JSONArray jsonArray = new JSONArray(extras.getString(NotificationConstant.ACTIONS));

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String action = jsonObject.getString(ActionConstant.ACTION);
                    String text = jsonObject.getString(ActionConstant.TEXT);

                    Intent intentAction = new Intent();
                    intentAction.setAction(BroadcastNotificationConstant.ACTION);
                    intentAction.putExtra(ActionConstant.class.toString(), action);

                    PendingIntent pendingIntent = PendingIntent
                            .getBroadcast(context, i, intentAction, PendingIntent.FLAG_UPDATE_CURRENT);

                    notificationBuilder.addAction(0, text, pendingIntent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the id of the application of the default icon.
     *
     * @param context Application context.
     *
     * @return Application icon id.
     */
    private int getNotificationIcon(Context context) {
        String packageName = context.getApplicationContext().getPackageName();
        PackageManager packageManager = context.getPackageManager();

        int iconResource = 0;

        try {
            ApplicationInfo applicationInfo =
                    packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            iconResource = applicationInfo.icon;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return iconResource;

    }

}
