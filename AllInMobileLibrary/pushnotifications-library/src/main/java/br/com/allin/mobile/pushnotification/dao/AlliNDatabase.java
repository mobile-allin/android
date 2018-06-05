package br.com.allin.mobile.pushnotification.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import br.com.allin.mobile.pushnotification.entity.allin.AICache;
import br.com.allin.mobile.pushnotification.entity.allin.AIMessage;
import br.com.allin.mobile.pushnotification.identifiers.DatabaseIdentifier;

/**
 * Created by lucasrodrigues on 05/04/17.
 */

@Database(
        version = 1,
        entities = {
                AICache.class,
                AIMessage.class
        },
        exportSchema = false)
public abstract class AlliNDatabase extends RoomDatabase {
    public abstract CacheDAO cacheTable();

    public abstract MessageDAO messageTable();

    private static AlliNDatabase instance;

    public static void init(Context context) {
        if (instance == null && context != null) {
            instance = Room.databaseBuilder(context, AlliNDatabase.class,
                    DatabaseIdentifier.DB_NAME).allowMainThreadQueries().build();
        }
    }

    public static AlliNDatabase get() {
        return instance;
    }
}
