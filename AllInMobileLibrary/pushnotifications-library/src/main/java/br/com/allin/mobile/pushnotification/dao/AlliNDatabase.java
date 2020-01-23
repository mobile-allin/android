package br.com.allin.mobile.pushnotification.dao;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import br.com.allin.mobile.pushnotification.entity.allin.AICache;
import br.com.allin.mobile.pushnotification.entity.allin.AINotification;
import br.com.allin.mobile.pushnotification.identifiers.DatabaseIdentifier;

/**
 * Created by lucasrodrigues on 05/04/17.
 */

@Database(
        version = 3,
        entities = {
                AICache.class, AINotification.class,
        },
        exportSchema = false)
public abstract class AlliNDatabase extends RoomDatabase {
    public abstract CacheDAO cacheTable();
    public abstract NotificationDAO notificationTable();

    private static AlliNDatabase instance;

    public static void initialize(@NonNull Context context) {
        if (AlliNDatabase.instance == null) {
            AlliNDatabase.instance =
                    Room.databaseBuilder(context, AlliNDatabase.class, DatabaseIdentifier.DB_NAME)
                            .fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
    }

    public static AlliNDatabase get() {
        return AlliNDatabase.instance;
    }
}
