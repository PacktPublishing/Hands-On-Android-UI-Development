package com.packtpub.claim.widget;

import android.content.Context;

import android.os.Parcel;
import android.os.Parcelable;

import android.support.annotation.Nullable;

import android.text.TextUtils;

import android.util.AttributeSet;

import android.view.LayoutInflater;

import android.widget.TextView;
import android.widget.LinearLayout;

import com.packtpub.claim.R;
import com.packtpub.claim.ui.DatePickerWrapper;

import java.util.Date;

/**
 * Created by jason on 2017/11/07.
 */
public class DatePickerLayout extends LinearLayout {

    private TextView label;
    private DatePickerWrapper wrapper;

    void initialize(final Context context) {
        setOrientation(VERTICAL);

        LayoutInflater.from(context).inflate(R.layout.widget_date_picker, this, true);

        label = (TextView) getChildAt(0);
        wrapper = new DatePickerWrapper((TextView) getChildAt(1));
    }

    public DatePickerLayout(
            final Context context) {

        super(context);
        initialize(context);
    }

    public DatePickerLayout(
            final Context context,
            final @Nullable AttributeSet attrs) {

        super(context, attrs);
        initialize(context);
    }

    public DatePickerLayout(
            final Context context,
            final @Nullable AttributeSet attrs,
            final int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    public void setDate(final Date date) {
        wrapper.setDate(date);
    }

    public Date getDate() {
        return wrapper.getDate();
    }

    public void setLabel(final CharSequence text) {
        label.setText(text);
    }

    public void setLabel(final int resid) {
        label.setText(resid);
    }

    public CharSequence getLabel() {
        return label.getText();
    }

    private static class SavedState extends BaseSavedState {

        final long timestamp;
        final CharSequence label;

        public SavedState(
                final Parcelable superState,
                final long timestamp,
                final CharSequence label) {

            super(superState);

            this.timestamp = timestamp;
            this.label = label;
        }

        SavedState(final Parcel in) {
            super(in);

            this.timestamp = in.readLong();
            this.label = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        }

        @Override
        public void writeToParcel(final Parcel out, final int flags) {
            super.writeToParcel(out, flags);

            out.writeLong(timestamp);
            TextUtils.writeToParcel(label, out, flags);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(final Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), getDate().getTime(), getLabel());
    }

    @Override
    protected void onRestoreInstanceState(final Parcelable state) {
        final SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        setDate(new Date(savedState.timestamp));
        setLabel(savedState.label);
    }

}
