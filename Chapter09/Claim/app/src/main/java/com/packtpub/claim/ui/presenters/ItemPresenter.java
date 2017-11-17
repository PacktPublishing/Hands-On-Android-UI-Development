package com.packtpub.claim.ui.presenters;

import android.content.Context;

import android.content.res.Resources;

import android.graphics.drawable.Drawable;

import com.packtpub.claim.R;

import com.packtpub.claim.model.Category;

import java.text.DateFormat;

import java.util.Date;

/**
 * Created by jason on 2017/11/09.
 */
public class ItemPresenter {

    private final Context context;

    public ItemPresenter(final Context context) {
        this.context = context;
    }

    public String formatAmount(final double amount) {
        return amount == 0
                ? ""
                : amount == (int) amount
                ? Integer.toString((int) amount)
                : String.format("%.2f", amount);
    }

    public Drawable getCategoryIcon(final Category category) {
        final Resources resources = context.getResources();

        switch (category) {
            case ACCOMMODATION:
                return resources.getDrawable(R.drawable.ic_hotel_black);
            case FOOD:
                return resources.getDrawable(R.drawable.ic_food_black);
            case TRANSPORT:
                return resources.getDrawable(R.drawable.ic_transport_black);
            case ENTERTAINMENT:
                return
                        resources.getDrawable(R.drawable.ic_entertainment_black);
            case BUSINESS:
                return resources.getDrawable(R.drawable.ic_business_black);
            case OTHER:
            default:
                return resources.getDrawable(R.drawable.ic_other_black);
        }
    }

    private DateFormat dateFormat;

    public String formatDate(final Date date) {
        if (dateFormat == null) {
            dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        }

        return dateFormat.format(date);
    }

}