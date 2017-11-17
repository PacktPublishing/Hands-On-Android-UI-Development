package com.packtpub.claim;

import android.app.Application;

import android.arch.persistence.room.Room;

import com.packtpub.claim.model.db.ClaimDatabase;

/**
 * Created by jason on 2017/11/09.
 */
public class ClaimApplication extends Application {

    private static ClaimDatabase DATABASE;

    @Override
    public void onCreate() {
        super.onCreate();

        DATABASE = Room.databaseBuilder(
                this, /* Context */
                ClaimDatabase.class, /* Abstract Database Class */
                "Claims" /* Filename */
        ).build();
    }

    public static ClaimDatabase getClaimDatabase() {
        return DATABASE;
    }

}