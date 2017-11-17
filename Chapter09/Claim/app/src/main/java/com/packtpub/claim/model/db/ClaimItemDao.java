package com.packtpub.claim.model.db;

import android.arch.lifecycle.LiveData;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

import com.packtpub.claim.model.ClaimItem;

import java.util.List;

/**
 * Created by jason on 2017/11/09.
 */
@Dao
public interface ClaimItemDao {

    @Query("SELECT * FROM claimitem ORDER BY timestamp DESC")
    LiveData<List<ClaimItem>> selectAll();

    @Insert
    long insert(ClaimItem item);

    @Update
    void update(ClaimItem item);

    @Delete
    void delete(ClaimItem item);

}