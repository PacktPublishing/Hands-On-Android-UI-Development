package com.packtpub.claim.widget;

import android.content.Context;

import android.graphics.Rect;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;

import android.support.v7.graphics.Palette;

import android.support.v7.widget.CardView;

import android.util.AttributeSet;

import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by jason on 2017/11/11.
 */
public class ColorizedCardView extends CardView implements Palette.PaletteAsyncListener {

    public ColorizedCardView(final Context context) {
        super(context);
    }

    public ColorizedCardView(
            final Context context,
            final AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorizedCardView(
            final Context context,
            final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    static Collection<TextView> findTextViews(
            final ViewGroup viewGroup,
            final Collection<TextView> textViews) {

        final int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = viewGroup.getChildAt(i);

            if (child instanceof ViewGroup) {
                findTextViews((ViewGroup) child, textViews);
            } else if (child instanceof TextView && child.getBackground() == null) {
                // we only grab the TextView instances with a transparent background
                textViews.add((TextView) child);
            }
        }

        return textViews;
    }

    public void setSwatch(final Palette.Swatch swatch) {
        setCardBackgroundColor(swatch.getRgb());

        final Collection<TextView> textViews = findTextViews(
                this, new ArrayList<TextView>()
        );

        if (!textViews.isEmpty()) {
            for (final TextView textView : textViews) {
                textView.setTextColor(swatch.getBodyTextColor());
            }
        }
    }

    public void setPalette(final Palette palette) {
        if (palette.getLightMutedSwatch() != null) {
            setSwatch(palette.getLightMutedSwatch());
        } else if (palette.getLightVibrantSwatch() != null) {
            setSwatch(palette.getLightVibrantSwatch());
        } else if (palette.getDarkMutedSwatch() != null) {
            setSwatch(palette.getDarkMutedSwatch());
        } else if (palette.getDarkVibrantSwatch() != null) {
            setSwatch(palette.getDarkVibrantSwatch());
        }
    }

    public void setColorizeBitmap(final Bitmap image) {
        new Palette.Builder(image).generate(this);
    }

    @Override
    public void onGenerated(final Palette palette) {
        setPalette(palette);
    }

    private Bitmap renderDrawable(final Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        final Rect bounds = drawable.getBounds();
        final Bitmap bitmap = Bitmap.createBitmap(
                bounds.width(),
                bounds.height(),
                Bitmap.Config.ARGB_8888
        );

        final Canvas canvas = new Canvas(bitmap);
        canvas.translate(-bounds.left, -bounds.top);
        drawable.draw(canvas);

        return bitmap;
    }

    public void setColorizeDrawable(final Drawable drawable) {
        setColorizeBitmap(renderDrawable(drawable));
    }

}
