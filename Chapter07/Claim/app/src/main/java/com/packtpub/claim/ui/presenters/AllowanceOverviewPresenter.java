package com.packtpub.claim.ui.presenters;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.LifecycleOwner;

import android.databinding.ObservableInt;
import android.databinding.ObservableField;

import android.support.v4.util.Pair;

import com.packtpub.claim.ClaimApplication;

import com.packtpub.claim.model.ClaimItem;

import com.packtpub.claim.util.ActionCommand;

import java.util.List;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by jason on 2017/11/08.
 */
public class AllowanceOverviewPresenter {

    public static class SpendingStats {
        public final int total;
        public final int today;
        public final int thisWeek;

        SpendingStats(
                final int total,
                final int today,
                final int thisWeek) {
            this.total = total;
            this.today = today;
            this.thisWeek = thisWeek;
        }
    }

    private final UpdateSpendingStatsCommand updateSpendStatsCommand = new UpdateSpendingStatsCommand();

    public final ObservableField<SpendingStats> spendingStats = new ObservableField<>();

    public final ObservableInt allowance = new ObservableInt();

    public AllowanceOverviewPresenter(
            final LifecycleOwner lifecycleOwner,
            final int allowance) {

        ClaimApplication.getClaimDatabase()
                .claimItemDao()
                .selectAll()
                .observe(lifecycleOwner, new Observer<List<ClaimItem>>() {
                    @Override
                    public void onChanged(final List<ClaimItem> claimItems) {
                        updateSpendStatsCommand.exec(claimItems);
                    }
                });

        this.allowance.set(allowance);
    }

    public void updateAllowance(final CharSequence newAllowance) {
        try {
            allowance.set(Integer.parseInt(newAllowance.toString()));
        } catch (final RuntimeException ex) {
            //ignore
            allowance.set(0);
        }
    }

    private class UpdateSpendingStatsCommand extends ActionCommand<List<ClaimItem>, SpendingStats> {

        Pair<Date, Date> getThisWeek() {
            final GregorianCalendar today = new GregorianCalendar();
            today.set(Calendar.HOUR_OF_DAY, today.getActualMaximum(Calendar.HOUR_OF_DAY));
            today.set(Calendar.MINUTE, today.getActualMaximum(Calendar.MINUTE));
            today.set(Calendar.SECOND, today.getActualMaximum(Calendar.SECOND));
            today.set(Calendar.MILLISECOND, today.getActualMaximum(Calendar.MILLISECOND));

            final Date end = today.getTime();
            today.add(Calendar.DATE, -(today.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY));

            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            return new Pair<>(today.getTime(), end);
        }

        Pair<Date, Date> getToday() {
            final GregorianCalendar today = new GregorianCalendar();
            today.set(Calendar.HOUR_OF_DAY, today.getActualMaximum(Calendar.HOUR_OF_DAY));
            today.set(Calendar.MINUTE, today.getActualMaximum(Calendar.MINUTE));
            today.set(Calendar.SECOND, today.getActualMaximum(Calendar.SECOND));
            today.set(Calendar.MILLISECOND, today.getActualMaximum(Calendar.MILLISECOND));

            final Date end = today.getTime();
            today.add(Calendar.DATE, -1);
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            return new Pair<>(today.getTime(), end);
        }

        public SpendingStats onBackground(final List<ClaimItem> items) {
            final Pair<Date, Date> today = getToday();
            final Pair<Date, Date> thisWeek = getThisWeek();

            double spentTotal = 0;
            double spentToday = 0;
            double spentThisWeek = 0;

            for (int i = 0; i < items.size(); i++) {
                final ClaimItem item = items.get(i);

                spentTotal += item.amount;

                if (item.getTimestamp().compareTo(thisWeek.first) >= 0
                        && item.getTimestamp().compareTo(thisWeek.second) <= 0) {

                    spentThisWeek += item.amount;
                }

                if (item.getTimestamp().compareTo(today.first) >= 0
                        && item.getTimestamp().compareTo(today.second) <= 0) {

                    spentToday += item.amount;
                }
            }

            // for stats we round everything to integers
            return new SpendingStats(
                    (int) spentTotal,
                    (int) spentToday,
                    (int) spentThisWeek
            );
        }

        public void onForeground(final SpendingStats newStats) {
            spendingStats.set(newStats);
        }
    }

}