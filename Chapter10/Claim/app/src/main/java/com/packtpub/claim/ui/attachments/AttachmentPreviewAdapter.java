package com.packtpub.claim.ui.attachments;

import android.support.v4.view.PagerAdapter;

import android.view.View;
import android.view.ViewGroup;

import com.packtpub.claim.model.Attachment;

import com.packtpub.claim.widget.AttachmentPreview;

import java.util.List;
import java.util.Collections;

/**
 * Created by jason on 2017/11/07.
 */
public class AttachmentPreviewAdapter extends PagerAdapter {

    private List<Attachment> attachments = Collections.emptyList();

    public int getCount() {
        return attachments.size();
    }

    public void setAttachments(final List<Attachment> attachments) {
        this.attachments = attachments != null
                ? attachments
                : Collections.<Attachment>emptyList();

        notifyDataSetChanged();
    }

    public Object instantiateItem(final ViewGroup container, final int position) {
        final AttachmentPreview preview = new AttachmentPreview(container.getContext());

        preview.setAttachment(attachments.get(position));
        container.addView(preview);

        return attachments.get(position);
    }

    public void destroyItem(final ViewGroup container, final int position, final Object object) {

        for (int i = 0; i < container.getChildCount(); i++) {
            if (((AttachmentPreview) container.getChildAt(i)).getAttachment() == object) {
                container.removeViewAt(i);
                break;
            }
        }
    }

    public boolean isViewFromObject(final View view, final Object o) {
        return (view instanceof AttachmentPreview) && (((AttachmentPreview) view).getAttachment() == o);
    }
}
