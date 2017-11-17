package com.packtpub.claim.ui.presenters;

import android.databinding.Observable;
import android.databinding.ObservableField;

import android.support.v4.util.Pair;

import com.packtpub.claim.model.Allowance;

import com.packtpub.claim.util.ActionCommand;

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

    public final ObservableField<SpendingStats> spendingStats = new ObservableField<>();

    private final UpdateSpendingStatsCommand updateSpendStatsCommand = new UpdateSpendingStatsCommand();

    private final Observable.OnPropertyChangedCallback allowanceObserver =
            new Observable.OnPropertyChangedCallback() {
                public void onPropertyChanged(
                        final Observable observable,
                        final int propertyId) {

                    updateSpendStatsCommand.exec(allowance);
                }
            };

    public final Allowance allowance;

    public AllowanceOverviewPresenter(final Allowance allowance) {
        this.allowance = allowance;
        this.allowance.addOnPropertyChangedCallback(allowanceObserver);
    }

    public void updateAllowance(final CharSequence newAllowance) {
        try {
            allowance.setAmountPerDay(Integer.parseInt(newAllowance.toString()));
        } catch (final RuntimeException ex) {
            //ignore
            allowance.setAmountPerDay(0);
        }
    }

    public void detach() {
        allowance.removeOnPropertyChangedCallback(allowanceObserver);
    }

    private class UpdateSpendingStatsCommand extends ActionCommand<Allowance, SpendingStats> {

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

        public SpendingStats onBackground(final Allowance allowance) throws
                Exception {
            final Pair<Date, Date> today = getToday();
            final Pair<Date, Date> thisWeek = getThisWeek();

            // for stats we round everything to integers
            return new SpendingStats(
                    (int) allowance.getTotalSpent(),
                    (int) allowance.getAmountSpent(today.first, today.second),
                    (int) allowance.getAmountSpent(thisWeek.first, thisWeek.second)
            );
        }

        public void onForeground(final SpendingStats newStats) {
            spendingStats.set(newStats);
        }
    }

}