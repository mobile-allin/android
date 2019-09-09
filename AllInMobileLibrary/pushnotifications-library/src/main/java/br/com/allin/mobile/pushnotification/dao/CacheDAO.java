package br.com.allin.mobile.pushnotification.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import br.com.allin.mobile.pushnotification.entity.allin.AICache;

/**
 * Class that manages the AICache database requests
 */
@Dao
public interface CacheDAO {
    @Query("SELECT * FROM cache")
    List<AICache> get();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AICache cache);

    @Delete
    void delete(AICache cache);

    @Query("DELETE FROM cache WHERE id = :id")
    void deleteById(long id);
}