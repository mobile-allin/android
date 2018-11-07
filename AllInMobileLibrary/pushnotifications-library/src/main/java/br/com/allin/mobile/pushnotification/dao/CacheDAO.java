package br.com.allin.mobile.pushnotification.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

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