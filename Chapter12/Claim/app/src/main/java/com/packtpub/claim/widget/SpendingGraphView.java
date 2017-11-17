package com.packtpub.claim.widget;

import android.content.Context;

import android.content.res.TypedArray;

import android.graphics.Path;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Canvas;

import android.util.AttributeSet;

import android.view.View;

import com.packtpub.claim.R;

/**
 * Created by jason on 2017/11/11.
 */
public class SpendingGraphView extends View {

    private int strokeColor = Color.GREEN;
    private int strokeWidth = 2;

    private double[] spendingPerDay;

    private Path path = null;
    private Paint paint = null;

    public SpendingGraphView(final Context context) {
        super(context);
        init(null, 0);
    }

    public SpendingGraphView(
            final Context context,
            final AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SpendingGraphView(
            final Context context,
            final AttributeSet attrs,
            final int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(final AttributeSet attrs, final int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.SpendingGraphView, defStyle, 0
        );

        strokeColor = a.getColor(
                R.styleable.SpendingGraphView_strokeColor,
                strokeColor
        );

        strokeWidth = a.getDimensionPixelSize(
                R.styleable.SpendingGraphView_strokeWidth,
                strokeWidth
        );

        a.recycle();
    }

    protected static double getMaximum(final double[] numbers) {
        double max = 0;

        for (final double n : numbers) {
            max = Math.max(max, n);
        }

        return max;
    }

    protected void invalidateGraph() {
        if (spendingPerDay == null || spendingPerDay.length <= 1) {
            path = null;
            paint = null;
            invalidate();

            return;
        }

        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();
        final int paddingRight = getPaddingRight();
        final int paddingBottom = getPaddingBottom();

        final int contentWidth =
                getWidth() - paddingLeft - paddingRight;
        final int contentHeight =
                getHeight() - paddingTop - paddingBottom;
        final int graphHeight =
                contentHeight - strokeWidth * 2;

        final double graphMaximum = getMaximum(spendingPerDay);

        final double stepSize = (double) contentWidth / (double) (spendingPerDay.length - 1);
        final double scale = (double) graphHeight / graphMaximum;

        path = new Path();
        path.moveTo(paddingLeft, paddingTop);

        paint = new Paint();
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(strokeColor);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);

        path.moveTo(paddingLeft, contentHeight - (float) (scale * spendingPerDay[0]));

        for (int i = 1; i < spendingPerDay.length; i++) {
            path.lineTo(
                    (float) (i * stepSize) + paddingLeft,
                    contentHeight - (float) (scale * spendingPerDay[i]));
        }

        invalidate();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        if (path == null || paint == null) {
            return;
        }

        canvas.drawPath(path, paint);
    }

    public void setSpendingPerDay(final double[] spendingPerDay) {
        this.spendingPerDay = spendingPerDay;
        invalidateGraph();
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(final int strokeColor) {
        this.strokeColor = strokeColor;
        invalidateGraph();
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(final int strokeWidth) {
        this.strokeWidth = strokeWidth;
        invalidateGraph();
    }

}
