package com.packtpub.claim.model.db;

import android.arch.lifecycle.LiveData;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

import com.packtpub.claim.model.Attachment;

import java.util.List;

/**
 * Created by jason on 2017/11/09.
 */
@Dao
public interface AttachmentDao {

    @Query("SELECT * FROM attachment WHERE claimItemId = :claimItemId")
    LiveData<List<Attachment>> selectForClaimItemId(final long claimItemId);

    @Insert
    long insert(Attachment attachment);

    @Update
    void update(Attachment attachment);

    @Delete
    void delete(Attachment attachment);

}