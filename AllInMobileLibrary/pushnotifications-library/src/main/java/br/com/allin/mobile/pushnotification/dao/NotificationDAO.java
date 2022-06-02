package br.com.allin.mobile.pushnotification.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import br.com.allin.mobile.pushnotification.entity.allin.AINotification;

@Dao
public interface NotificationDAO {
    @Query("SELECT * FROM notifications_history")
    List<AINotification> get();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AINotification notification);

    @Delete
    void delete(AINotification notification);

    @Query("DELETE FROM notifications_history WHERE idMessage = :id")
    void deleteById(String id);
}