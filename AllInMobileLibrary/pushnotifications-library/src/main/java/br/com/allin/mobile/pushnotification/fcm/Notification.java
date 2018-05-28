package br.com.allin.mobile.pushnotification.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.support.v7.app.NotificationCompat.Builder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.identifiers.ActionIdentifier;
import br.com.allin.mobile.pushnotification.identifiers.BroadcastNotificationIdentifier;
import br.com.allin.mobile.pushnotification.identifiers.PreferenceIdentifier;
import br.com.allin.mobile.pushnotification.identifiers.PushIdentifier;
import br.com.allin.mobile.pushnotification.entity.allin.AlMessage;
import br.com.allin.mobile.pushnotification.helper.PreferencesManager;
import br.com.allin.mobile.pushnotification.helper.Util;
import br.com.allin.mobile.pushnotification.http.DownloadImage;
import br.com.allin.mobile.pushnotification.http.DownloadImage.OnDownloadCompleted;

public class Notification {
    private WeakReference<Context> contextWeakReference;
    private PreferencesManager preferences;

    Notification(Context context) {
        this.preferences = new PreferencesManager(context);
        this.contextWeakReference = new WeakReference<>(context);
    }

    void showNotification(@NonNull final Bundle bundle) {
        if (bundle.containsKey(PushIdentifier.IMAGE)) {
            new DownloadImage(bundle.getString(PushIdentifier.IMAGE), new OnDownloadCompleted() {
                @Override
                public void onCompleted(Bitmap bitmap) {
                    showNotification(bitmap, bundle);
                }

                @Override
                public void onError() {
                    showNotification(null, bundle);
                }
            }).execute();
        } else {
            showNotification(null, bundle);
        }
    }

    private void showNotification(Bitmap bitmap, Bundle bundle) {
        String title = bundle.getString(PushIdentifier.SUBJECT);
        String content = bundle.getString(PushIdentifier.DESCRIPTION);

        if (!Util.isNullOrClear(title) && !Util.isNullOrClear(content)) {
            Context context = contextWeakReference.get();
            int color = preferences.getData(PreferenceIdentifier.BACKGROUND_NOTIFICATION, 0);
            int whiteIcon = preferences.getData(PreferenceIdentifier.WHITE_ICON_NOTIFICATION, 0);
            int icon = preferences.getData(PreferenceIdentifier.ICON_NOTIFICATION, 0);
            long idMessage = AlliNPush.getInstance().addMessage(new AlMessage(bundle));

            bundle.putLong(PushIdentifier.ID, idMessage);

            Intent intent = new Intent();
            intent.setAction(BroadcastNotificationIdentifier.ACTION);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = getPending(context, 0, intent);
            Builder builder = new Builder(context);

            if (icon == 0) {
                builder.setSmallIcon(whiteIcon != 0 ? whiteIcon : getNotificationIcon(context));
            } else {
                builder.setSmallIcon(whiteIcon)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), icon));
            }

            builder.setColor(color == 0 ? Color.TRANSPARENT : ContextCompat.getColor(context, color))
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setGroupSummary(true)
                    .setGroup("messages")
                    .setContentIntent(pendingIntent)
                    .setContentText(content)
                    .setContentTitle(title)
                    .setAutoCancel(true);

            if (bitmap != null) {
                builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap));
            } else {
                builder.setStyle(new NotificationCompat.BigTextStyle().bigText(content));
            }

            addActions(context, builder, bundle);

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.notify((int) idMessage, builder.build());
            }
        }
    }

    private void addActions(Context context, Builder notificationBuilder, Bundle extras) {
        if (extras.containsKey(PushIdentifier.ACTIONS)) {
            try {
                JSONArray jsonArray = new JSONArray(extras.getString(PushIdentifier.ACTIONS));

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String action = jsonObject.getString(ActionIdentifier.ACTION);
                    String text = jsonObject.getString(ActionIdentifier.TEXT);

                    Intent intent = new Intent();
                    intent.setAction(BroadcastNotificationIdentifier.ACTION);
                    intent.putExtra(PushIdentifier.ACTION, action);

                    notificationBuilder.addAction(0, text, getPending(context, i, intent));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int getNotificationIcon(Context context) {
        String packageName = context.getApplicationContext().getPackageName();
        PackageManager packageManager = context.getPackageManager();

        try {
            return packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA).icon;
        } catch (NameNotFoundException e) {
            return 0;
        }
    }

    private PendingIntent getPending(Context context, int code, Intent intent) {
        return PendingIntent.getBroadcast(context, code, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
