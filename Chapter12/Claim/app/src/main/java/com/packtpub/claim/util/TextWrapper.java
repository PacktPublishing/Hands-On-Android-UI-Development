package com.packtpub.claim.util;

import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;

/**
 * Created by jason on 2017/11/11.
 */
public abstract class TextWrapper<V extends View> {
    public final V view;

    public TextWrapper(final V view) {
        this.view = view;
    }

    public abstract void setText(CharSequence text);

    public abstract CharSequence getText();

    public static TextWrapper<TextView> wrap(final TextView tv) {
        return new TextWrapper<TextView>(tv) {
            @Override
            public void setText(final CharSequence text) {
                view.setText(text);
            }

            @Override
            public CharSequence getText() {
                return view.getText();
            }
        };
    }

    public static TextWrapper<TextSwitcher> wrap(final TextSwitcher ts) {
        return new TextWrapper<TextSwitcher>(ts) {
            @Override
            public void setText(final CharSequence text) {
                view.setText(text);
            }

            @Override
            public CharSequence getText() {
                return ((TextView) view.getCurrentView()).getText();
            }
        };
    }

    public static TextWrapper<?> wrap(final View v) {
        if (v instanceof TextView) {
            return wrap((TextView) v);
        } else if (v instanceof TextSwitcher) {
            return wrap((TextSwitcher) v);
        } else {
            throw new IllegalArgumentException("unknown text view: " + v);
        }
    }

}