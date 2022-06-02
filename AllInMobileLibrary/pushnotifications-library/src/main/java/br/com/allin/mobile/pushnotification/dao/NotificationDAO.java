package br.com.allin.mobile.pushnotification.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

<<<<<<< HEAD
import br.com.allin.mobile.pushnotification.entity.allin.AINotification;

@Dao
public interface NotificationDAO {
    @Query("SELECT * FROM notifications_history")
=======
import br.com.allin.mobile.pushnotification.entity.allin.AICache;
import br.com.allin.mobile.pushnotification.entity.allin.AINotification;

/**
 * Class that manages the AICache database requests
 */
@Dao
public interface NotificationDAO {
    @Query("SELECT * FROM notification")
>>>>>>> 350efc96fc364224c48e0b253b6f750ba8f711ce
    List<AINotification> get();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AINotification notification);

    @Delete
    void delete(AINotification notification);

<<<<<<< HEAD
    @Query("DELETE FROM notifications_history WHERE idMessage = :id")
    void deleteById(String id);
}

=======
    @Query("DELETE FROM notification WHERE idMessage = :id")
    void deleteById(long id);
}
>>>>>>> 350efc96fc364224c48e0b253b6f750ba8f711ce
