package br.com.allin.mobile.pushnotification.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import br.com.allin.mobile.pushnotification.entity.allin.AIMessage;

/**
 * Created by lucasrodrigues on 05/04/17.
 */

@Dao
public interface MessageDAO {
    @Query("SELECT * FROM message")
    List<AIMessage> get();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AIMessage message);

    @Delete
    void delete(AIMessage message);

    @Query("DELETE FROM message WHERE id = :id")
    void deleteById(long id);

    @Query("SELECT read FROM message WHERE id = :id")
    boolean hasBeenRead(long id);
}
