package com.packtpub.claim.model;

import android.support.annotation.IdRes;

import com.packtpub.claim.R;

/**
 * Created by jason on 2017/11/07.
 */
public enum Category {

    ACCOMMODATION(R.id.accommodation),
    FOOD(R.id.room_service),
    TRANSPORT(R.id.transport),
    ENTERTAINMENT(R.id.entertainment),
    BUSINESS(R.id.business),
    OTHER(R.id.other);

    @IdRes
    private final int idResource;

    Category(@IdRes final int idResource) {
        this.idResource = idResource;
    }

    @IdRes
    public int getIdResource() {
        return idResource;
    }

    public static Category forIdResource(@IdRes final int id) {
        for (final Category c : values()) {
            if (c.idResource == id) {
                return c;
            }
        }

        throw new IllegalArgumentException("No category for ID: " + id);
    }
}
