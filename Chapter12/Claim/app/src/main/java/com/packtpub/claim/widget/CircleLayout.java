package com.packtpub.claim.widget;

import android.content.Context;

import android.util.AttributeSet;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jason on 2017/11/11.
 */
public class CircleLayout extends ViewGroup {

    public CircleLayout(final Context context) {
        super(context);
    }

    public CircleLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleLayout(
            final Context context,
            final AttributeSet attrs,
            final int defStyleAttr) {

        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(
                MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec));
    }

    protected void onLayout(
            final boolean changed,
            final int left,
            final int top,
            final int right,
            final int bottom) {

        final int childCount = getChildCount();

        if (childCount == 0) {
            return;
        }

        final int width = right - left;
        final int height = bottom - top;

        // if we have children, we assume they're all the same size
        final int childrenWidth = getChildAt(0).getMeasuredWidth();
        final int childrenHeight = getChildAt(0).getMeasuredHeight();

        final int boxSize = Math.min(width - childrenWidth, height - childrenHeight);

        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            final int childWidth = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();

            final double x = Math.sin((Math.PI * 2.0) * ((double) i / (double) childCount));
            final double y = -Math.cos((Math.PI * 2.0) * ((double) i / (double) childCount));

            final int childLeft = (int) (x * (boxSize / 2)) + (width / 2) - (childWidth / 2);
            final int childTop = (int) (y * (boxSize / 2)) + (height / 2) - (childHeight / 2);
            final int childRight = childLeft + childWidth;
            final int childBottom = childTop + childHeight;

            child.layout(childLeft, childTop, childRight, childBottom);
        }
    }
}