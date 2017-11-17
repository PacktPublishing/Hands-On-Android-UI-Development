package com.packtpub.claim.widget;

import android.content.Context;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.support.v7.widget.CardView;

import android.util.AttributeSet;

import android.view.LayoutInflater;

import android.widget.ImageView;

import com.packtpub.claim.R;
import com.packtpub.claim.model.Attachment;
import com.packtpub.claim.util.ActionCommand;

/**
 * Created by jason on 2017/11/07.
 */

public class AttachmentPreview extends CardView {

    private Attachment attachment;
    private ImageView preview;

    public AttachmentPreview(
            final Context context) {

        super(context);
        initialize(context);
    }

    public AttachmentPreview(
            final Context context,
            final AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public AttachmentPreview(
            final Context context,
            final AttributeSet attrs,
            final int defStyleAttr) {

        super(context, attrs, defStyleAttr);
    }

    void initialize(final Context context) {
        LayoutInflater.from(context)
                .inflate(R.layout.widget_attachment_preview, this, true);
        preview = (ImageView) getChildAt(0);
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(final Attachment attachment) {
        this.attachment = attachment;
        preview.setImageDrawable(null);
        if (attachment != null) {
            new UpdatePreviewCommand().exec(attachment);
        }
    }

    private class UpdatePreviewCommand extends ActionCommand<Attachment, Drawable> {

        @Override
        public Drawable onBackground(final Attachment attachment) throws Exception {
            switch (attachment.getType()) {
                case IMAGE:
                    return new BitmapDrawable(getResources(), attachment.getFile().getAbsolutePath());
            }

            return getResources().getDrawable(R.drawable.ic_unknown_file_type);
        }

        @Override
        public void onForeground(final Drawable value) {
            preview.setImageDrawable(value);
        }
    }
}
