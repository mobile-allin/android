package br.com.allin.mobile.pushnotification.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import br.com.allin.mobile.pushnotification.entity.allin.AICache;
import br.com.allin.mobile.pushnotification.entity.allin.AINotification;

/**
 * Class that manages the AICache database requests
 */
@Dao
public interface NotificationDAO {
    @Query("SELECT * FROM notification")
    List<AINotification> get();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AINotification notification);

    @Delete
    void delete(AINotification notification);

    @Query("DELETE FROM notification WHERE idMessage = :id")
    void deleteById(long id);
}