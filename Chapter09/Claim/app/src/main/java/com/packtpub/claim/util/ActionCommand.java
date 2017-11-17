package com.packtpub.claim.util;

import android.os.Looper;
import android.os.Handler;
import android.os.AsyncTask;

import android.util.Log;

import java.util.concurrent.Executor;

/**
 * Created by jason on 2017/11/07.
 */
public abstract class ActionCommand<P, R> {

    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    public abstract R onBackground(final P value) throws Exception;

    public abstract void onForeground(final R value);

    public void onError(final Exception error) {
        Log.e(getClass().getSimpleName(), "Error while processing data", error);
    }

    public void exec(final P parameter) {
        exec(parameter, AsyncTask.SERIAL_EXECUTOR);
    }

    public void exec(final P parameter, final Executor background) {
        background.execute(new ActionCommandRunner(parameter, this));
    }

    private static class ActionCommandRunner implements Runnable {

        private static final int STATE_BACKGROUND = 1;
        private static final int STATE_FOREGROUND = 2;
        private static final int STATE_ERROR = 3;

        private int state = STATE_BACKGROUND;

        private final ActionCommand command;

        private Object value;

        ActionCommandRunner(
                final Object value,
                final ActionCommand command) {

            this.value = value;
            this.command = command;
        }

        void onBackground() {
            try {
                // our current "value" is the commands parameter
                this.value = command.onBackground(value);
                this.state = STATE_FOREGROUND;
            } catch (final Exception error) {
                this.value = error;
                this.state = STATE_ERROR;
            } finally {
                MAIN_HANDLER.post(this);
            }
        }

        void onForeground() {
            try {
                command.onForeground(value);
            } catch (final Exception error) {
                this.value = error;
                this.state = STATE_ERROR;

                // we go into an error state, and return to foreground to deliver it
                MAIN_HANDLER.post(this);
            }
        }

        void onError() {
            command.onError((Exception) value);
        }

        @Override
        public void run() {
            switch (state) {
                case STATE_BACKGROUND:
                    onBackground();
                    break;
                case STATE_FOREGROUND:
                    onForeground();
                    break;
                case STATE_ERROR:
                    onError();
                    break;
            }
        }
    }
}