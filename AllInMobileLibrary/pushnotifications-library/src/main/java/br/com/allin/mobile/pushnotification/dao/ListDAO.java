package br.com.allin.mobile.pushnotification.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import br.com.allin.mobile.pushnotification.entity.allin.AIList;

/**
 * Class that manages the AICache database requests
 */
@Dao
public interface ListDAO {
    @Query("SELECT count(*) FROM list WHERE md5 = :md5")
    int exist(String md5);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AIList list);
}