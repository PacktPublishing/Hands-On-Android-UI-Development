package com.packtpub.claim.ui;

import android.content.res.Resources;

import android.graphics.drawable.Drawable;

import android.support.v7.widget.RecyclerView;

import android.view.View;

import android.widget.TextView;
import android.widget.ImageView;

import com.packtpub.claim.R;

import com.packtpub.claim.model.Category;

import java.text.DateFormat;

import java.util.Locale;

/**
 * Created by jason on 2017/11/09.
 */
public class ClaimItemViewHolder extends RecyclerView.ViewHolder {

    private final ImageView categoryIcon;
    private final TextView description;
    private final TextView amount;
    private final TextView timestamp;
    private final DateFormat dateFormat;

    public ClaimItemViewHolder(final View claimItemCard) {
        super(claimItemCard);

        this.categoryIcon = claimItemCard.findViewById(R.id.item_category);
        this.description = claimItemCard.findViewById(R.id.item_description);
        this.amount = claimItemCard.findViewById(R.id.item_amount);
        this.timestamp = claimItemCard.findViewById(R.id.item_timestamp);
        this.dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
    }

    public Drawable getCategoryIcon(final Category category) {
        final Resources resources = itemView.getResources();

        switch (category) {
            case ACCOMMODATION:
                return resources.getDrawable(R.drawable.ic_hotel_black);
            case FOOD:
                return resources.getDrawable(R.drawable.ic_food_black);
            case TRANSPORT:
                return resources.getDrawable(R.drawable.ic_transport_black);
            case ENTERTAINMENT:
                return resources.getDrawable(R.drawable.ic_entertainment_black);
            case BUSINESS:
                return resources.getDrawable(R.drawable.ic_business_black);
            case OTHER:
            default:
                return resources.getDrawable(R.drawable.ic_other_black);
        }
    }

    public String formatAmount(final double amount) {
        return amount == 0
                ? ""
                : amount == (int) amount
                ? Integer.toString((int) amount)
                : String.format(Locale.getDefault(), "%.2f", amount);
    }

}