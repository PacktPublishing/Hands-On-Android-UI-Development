package com.packtpub.claim.widget;

import android.content.Context;

import android.graphics.Rect;
import android.graphics.Canvas;

import android.graphics.drawable.Drawable;

import android.util.AttributeSet;

import android.view.View;

/**
 * Created by jason on 2017/11/11.
 */
public class BouncingDrawablesView extends View {

    private Bouncer[] bouncers = null;
    private boolean running = false;

    public BouncingDrawablesView(
            final Context context) {
        super(context);
    }

    public BouncingDrawablesView(
            final Context context,
            final AttributeSet attrs) {
        super(context, attrs);
    }

    public BouncingDrawablesView(
            final Context context,
            final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        if (bouncers == null) {
            return;
        }

        for (final Bouncer bouncer : bouncers) {
            bouncer.draw(canvas);
        }
    }

    private final Runnable postNextFrame = new Runnable() {
        @Override
        public void run() {
            onNextFrame();
        }
    };

    void onNextFrame() {
        if (bouncers == null || !running) {
            return;
        }

        final Rect boundary = new Rect(
                getPaddingLeft(),
                getPaddingTop(),
                getWidth() - getPaddingLeft() - getPaddingRight(),
                getHeight() - getPaddingTop() - getPaddingBottom()
        );

        for (final Bouncer bouncer : bouncers) {
            bouncer.step(boundary);
        }

        invalidate();
        getHandler().postDelayed(postNextFrame, 16);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        running = true;

        post(postNextFrame);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        running = false;
    }

    public void setBouncers(final Bouncer[] bouncers) {
        this.bouncers = bouncers;
    }

    public Bouncer[] getBouncers() {
        return bouncers;
    }

    public static class Bouncer {
        final Drawable drawable;
        final Rect bounds;
        int speedX;
        int speedY;

        public Bouncer(
                final Drawable drawable,
                final int speedX,
                final int speedY) {

            this.drawable = drawable;
            this.bounds = drawable.copyBounds();
            this.speedX = speedX;
            this.speedY = speedY;
        }

        void step(final Rect boundary) {
            final int width = bounds.width();
            final int height = bounds.height();

            int nextLeft = bounds.left + speedX;
            int nextTop = bounds.top + speedY;

            if (nextLeft + width >= boundary.right) {
                speedX = -speedX;
                nextLeft = boundary.right - width;
            } else if (nextLeft < boundary.left) {
                speedX = -speedX;
                nextLeft = boundary.left;
            }

            if (nextTop + height >= boundary.bottom) {
                speedY = -speedY;
                nextTop = boundary.bottom - height;
            } else if (nextTop < boundary.top) {
                speedY = -speedY;
                nextTop = boundary.top;
            }

            bounds.set(
                    nextLeft,
                    nextTop,
                    nextLeft + width,
                    nextTop + height
            );
        }

        void draw(final Canvas canvas) {
            drawable.setBounds(bounds);
            drawable.draw(canvas);
        }
    }

}
