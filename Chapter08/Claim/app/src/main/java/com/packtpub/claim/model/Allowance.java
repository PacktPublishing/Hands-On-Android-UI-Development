package com.packtpub.claim.model;

import android.databinding.BaseObservable;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

/**
 * Created by jason on 2017/11/08.
 */
public class Allowance extends BaseObservable implements Parcelable {

    private int amountPerDay;

    private final List<ClaimItem> items = new ArrayList<>();

    public Allowance(final int amountPerDay) {
        this.amountPerDay = amountPerDay;
    }

    protected Allowance(final Parcel in) {
        amountPerDay = in.readInt();
        in.readTypedList(items, ClaimItem.CREATOR);
    }

    public int getAmountPerDay() {
        return amountPerDay;
    }

    public void setAmountPerDay(final int amountPerDay) {
        this.amountPerDay = amountPerDay;
        notifyChange();
    }

    public Date getStartDate() {
        return items.get(items.size() - 1).getTimestamp();
    }

    public Date getEndDate() {
        return items.get(0).getTimestamp();
    }

    public double getTotalSpent() {
        double total = 0;

        for (final ClaimItem item : items) {
            total += item.getAmount();
        }

        return total;
    }

    public double getAmountSpent(final Date from, final Date to) {
        double spent = 0;

        for (int i = 0; i < items.size(); i++) {
            final ClaimItem item = items.get(i);
            if (item.getTimestamp().compareTo(from) >= 0
                    && item.getTimestamp().compareTo(to) <= 0) {

                spent += item.getAmount();
            }
        }

        return spent;
    }

    public void addClaimItem(final ClaimItem item) {
        items.add(item);

        Collections.sort(items, Collections.reverseOrder(new Comparator<ClaimItem>() {
            @Override
            public int compare(final ClaimItem o1, final ClaimItem o2) {
                return o1.getTimestamp().compareTo(o2.getTimestamp());
            }
        }));

        notifyChange();
    }

    public void removeClaimItem(final ClaimItem item) {
        items.remove(item);
        notifyChange();
    }

    public int getClaimItemCount() {
        return items.size();
    }

    public ClaimItem getClaimItem(final int index) {
        return items.get(index);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(amountPerDay);
        dest.writeTypedList(items);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Allowance> CREATOR = new Creator<Allowance>() {

        @Override
        public Allowance createFromParcel(Parcel in) {
            return new Allowance(in);
        }

        @Override
        public Allowance[] newArray(int size) {
            return new Allowance[size];
        }
    };
}
