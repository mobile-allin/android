package br.com.allin.mobile.pushnotification.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import br.com.allin.mobile.pushnotification.entity.allin.AICache;
import br.com.allin.mobile.pushnotification.identifiers.DatabaseIdentifier;

/**
 * Created by lucasrodrigues on 05/04/17.
 */

@Database(
        version = 1,
        entities = {
                AICache.class
        },
        exportSchema = false)
public abstract class AlliNDatabase extends RoomDatabase {
    public abstract CacheDAO cacheTable();

    private static AlliNDatabase instance;

    public static void initialize(@NonNull Context context) {
        if (AlliNDatabase.instance == null) {
            AlliNDatabase.instance = Room.databaseBuilder(context, AlliNDatabase.class,
                    DatabaseIdentifier.DB_NAME).allowMainThreadQueries().build();
        }
    }

    public static AlliNDatabase get() {
        return AlliNDatabase.instance;
    }
}
