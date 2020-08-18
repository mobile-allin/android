package br.com.allin.mobile.pushnotification.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.helper.Util;
import br.com.allin.mobile.pushnotification.http.DownloadImage;
import br.com.allin.mobile.pushnotification.http.DownloadImage.OnDownloadCompleted;
import br.com.allin.mobile.pushnotification.identifiers.PushIdentifier;
import br.com.allin.mobile.pushnotification.service.allin.NotificationService;

public class Notification {
    public Notification(Context context) {
        AlliNPush.getInstance(context);
    }

    public void showNotification(@NonNull final RemoteMessage remoteMessage) {
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

        String notificationId = bundle.getString(PushIdentifier.ID);
        int id = 1;

        if (notificationId != null && TextUtils.isDigitsOnly(notificationId)) {
            id = Integer.parseInt(notificationId);
        }

        String title = bundle.getString(PushIdentifier.TITLE);
        String body = bundle.getString(PushIdentifier.BODY);

        if (!Util.isEmpty(title) && !Util.isEmpty(body)) {
            NotificationService.insert(id, title, body);

            Context context = AlliNPush.getInstance().getContext();

            Intent intent = new Intent(context, Register.class);
            intent.putExtras(bundle);

            String channelId = this.getChannelId(context);
            int icon = this.getBigIcon(context);
            int whiteIcon = this.getWhiteIcon(context);
            int color = this.getColor(context);

            PendingIntent pendingIntent = getPending(context, 0, intent);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);

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

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager != null) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel 001", NotificationManager.IMPORTANCE_HIGH);
                channel.enableVibration(true);
                channel.enableLights(true);
                notificationManager.createNotificationChannel(channel);
            }

            if (notificationManager != null) {
                notificationManager.notify(id, builder.build());
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
        return PendingIntent.getActivity(context, code,
                intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
    }

    private Bundle generateBundle(RemoteMessage remoteMessage) {
        Bundle bundle = new Bundle();

        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
            bundle.putString(entry.getKey(), entry.getValue());
        }

        if (remoteMessage.getNotification() != null) {
            bundle.putString(PushIdentifier.TITLE, remoteMessage.getNotification().getTitle());
            bundle.putString(PushIdentifier.BODY, remoteMessage.getNotification().getBody());
        }

        return bundle;
    }

    private String getChannelId(Context context) {
        try {
            String key = "br.com.allin.messaging.messaging.notification_channel_id";

            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();

            ApplicationInfo applicationInfo = packageManager
                    .getApplicationInfo(packageName, PackageManager.GET_META_DATA);

            return applicationInfo.metaData.getString(key);
        } catch (Exception e) {
            return "notify_001";
        }
    }

    private int getWhiteIcon(Context context) {
        try {
            String key = "br.com.allin.messaging.notification_white_icon";

            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();

            ApplicationInfo applicationInfo = packageManager
                    .getApplicationInfo(packageName, PackageManager.GET_META_DATA);

            return applicationInfo.metaData.getInt(key, 0);
        } catch (Exception e) {
            return 0;
        }
    }

    private int getBigIcon(Context context) {
        try {
            String key = "br.com.allin.messaging.notification_big_icon";

            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();

            ApplicationInfo applicationInfo = packageManager
                    .getApplicationInfo(packageName, PackageManager.GET_META_DATA);

            return applicationInfo.metaData.getInt(key, 0);
        } catch (Exception e) {
            return 0;
        }
    }

    private int getColor(Context context) {
        try {
            String key = "br.com.allin.messaging.messaging.notification_color";

            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();

            ApplicationInfo applicationInfo = packageManager
                    .getApplicationInfo(packageName, PackageManager.GET_META_DATA);

            return applicationInfo.metaData.getInt(key, 0);
        } catch (Exception e) {
            return 0;
        }
    }
}