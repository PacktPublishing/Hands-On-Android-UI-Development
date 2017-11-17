package com.packtpub.claim.ui.attachments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.LifecycleOwner;

import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;

import com.packtpub.claim.model.Attachment;
import com.packtpub.claim.widget.AttachmentPreview;

import java.util.List;
import java.util.Collections;

/**
 * Created by jason on 2017/11/09.
 */
public class AttachmentListAdapter extends BaseAdapter {

    private List<Attachment> attachments = Collections.emptyList();

    public AttachmentListAdapter(
            final LifecycleOwner lifecycleOwner,
            final LiveData<List<Attachment>> attachments) {

        attachments.observe(lifecycleOwner, new Observer<List<Attachment>>() {
            @Override
            public void onChanged(final List<Attachment> attachments) {
                AttachmentListAdapter.this.attachments =
                        attachments != null
                                ? attachments
                                : Collections.<Attachment>emptyList();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getCount() {
        return attachments.size();
    }

    @Override
    public Object getItem(final int i) {
        return attachments.get(i);
    }

    @Override
    public long getItemId(final int i) {
        return attachments.get(i).id;
    }

    @Override
    public View getView(
            final int i,
            final View view,
            final ViewGroup viewGroup) {

        AttachmentPreview preview = (AttachmentPreview) view;
        if (preview == null) {
            preview = new AttachmentPreview(viewGroup.getContext());
        }

        preview.setAttachment(attachments.get(i));

        return preview;
    }

}
