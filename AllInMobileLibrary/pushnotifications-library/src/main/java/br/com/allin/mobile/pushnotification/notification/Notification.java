package br.com.allin.mobile.pushnotification.notification;

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
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.Map;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.entity.allin.AIMessage;
import br.com.allin.mobile.pushnotification.identifiers.ActionIdentifier;
import br.com.allin.mobile.pushnotification.identifiers.BroadcastNotificationIdentifier;
import br.com.allin.mobile.pushnotification.identifiers.PreferenceIdentifier;
import br.com.allin.mobile.pushnotification.identifiers.PushIdentifier;
import br.com.allin.mobile.pushnotification.helper.PreferencesManager;
import br.com.allin.mobile.pushnotification.helper.Util;
import br.com.allin.mobile.pushnotification.http.DownloadImage;
import br.com.allin.mobile.pushnotification.http.DownloadImage.OnDownloadCompleted;

public class Notification {
    private PreferencesManager preferences;

    Notification(Context context) {
        AlliNPush.getInstance(context);

        this.preferences = new PreferencesManager(context);
    }

    void showNotification(@NonNull final RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();

        if (data.containsKey(PushIdentifier.IMAGE)) {
            new DownloadImage(data.get(PushIdentifier.IMAGE), new OnDownloadCompleted() {
                @Override
                public void onCompleted(Bitmap bitmap) {
                    showNotification(bitmap, remoteMessage);
                }

                @Override
                public void onError() {
                    showNotification(null, remoteMessage);
                }
            }).execute();
        } else {
            showNotification(null, remoteMessage);
        }
    }

    private void showNotification(Bitmap bitmap, RemoteMessage remoteMessage) {
        Bundle bundle = generateBundle(remoteMessage);

        String title = bundle.getString(PushIdentifier.TITLE);
        String body = bundle.getString(PushIdentifier.BODY);

        if (!Util.isNullOrClear(title) && !Util.isNullOrClear(body)) {
            Context context = AlliNPush.getInstance().getContext();

            int color = preferences.getData(PreferenceIdentifier.BACKGROUND_NOTIFICATION, 0);
            int whiteIcon = preferences.getData(PreferenceIdentifier.WHITE_ICON_NOTIFICATION, 0);
            int icon = preferences.getData(PreferenceIdentifier.ICON_NOTIFICATION, 0);

            AlliNPush.getInstance().addMessage(new AIMessage(bundle));

            Intent intent = new Intent();
            intent.setAction(BroadcastNotificationIdentifier.ACTION);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = getPending(context, 0, intent);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "");

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
                    .setContentText(body)
                    .setContentTitle(title)
                    .setAutoCancel(true);

            if (bitmap != null) {
                builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap));
            } else {
                builder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));
            }

            addActions(context, builder, bundle);

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.notify((int) bundle.getLong(PushIdentifier.ID), builder.build());
            }
        }
    }

    private void addActions(Context context, NotificationCompat.Builder notificationBuilder, Bundle extras) {
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

    private Bundle generateBundle(RemoteMessage remoteMessage) {
        Map<String, String> map = remoteMessage.getData();

        if (map.containsKey(PushIdentifier.URL_SCHEME)) {
            String scheme = map.get(PushIdentifier.URL_SCHEME);

            try {
                scheme = URLDecoder.decode(scheme, "UTF-8");
            } catch (Exception e) {
                Log.e(MessagingService.class.toString(), "ERRO IN DECODE URL");
            } finally {
                if (scheme != null && scheme.contains("##id_push##")) {
                    String md5DeviceToken = Util.md5(AlliNPush.getInstance().getDeviceToken());

                    scheme = scheme.replace("##id_push##", md5DeviceToken);
                }

                map.put(PushIdentifier.URL_SCHEME, scheme);
            }
        }

        Bundle bundle = new Bundle();

        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
            bundle.putString(entry.getKey(), entry.getValue());
        }

        RemoteMessage.Notification notification = remoteMessage.getNotification();

        if (notification != null) {
            bundle.putString(PushIdentifier.TITLE, notification.getTitle());
            bundle.putString(PushIdentifier.BODY, notification.getBody());
        }

        return bundle;
    }
}