package com.packtpub.claim.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LifecycleOwner;

import android.content.Context;

import android.databinding.DataBindingUtil;

import android.support.v7.widget.RecyclerView;

import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.packtpub.claim.R;

import com.packtpub.claim.model.ClaimItem;

import com.packtpub.claim.ui.presenters.ItemPresenter;

import java.util.List;
import java.util.Collections;

/**
 * Created by jason on 2017/11/09.
 */
public class ClaimItemAdapter extends RecyclerView.Adapter<DataBoundViewHolder<ItemPresenter, ClaimItem>> {

    private final LayoutInflater layoutInflater;

    private final ItemPresenter itemPresenter;

    private List<ClaimItem> items = Collections.emptyList();

    public ClaimItemAdapter(
            final Context context,
            final LifecycleOwner owner,
            final LiveData<List<ClaimItem>> liveItems) {

        this.layoutInflater = LayoutInflater.from(context);
        this.itemPresenter = new ItemPresenter(context);

        liveItems.observe(owner, new Observer<List<ClaimItem>>() {
            public void onChanged(final List<ClaimItem> claimItems) {
                ClaimItemAdapter.this.items = (claimItems != null)
                        ? claimItems
                        : Collections.<ClaimItem>emptyList();

                ClaimItemAdapter.this.notifyDataSetChanged();
            }
        });
    }

    public DataBoundViewHolder<ItemPresenter, ClaimItem> onCreateViewHolder(
            final ViewGroup parent,
            final int viewType) {

        return new DataBoundViewHolder<>(
                DataBindingUtil.inflate(
                        layoutInflater,
                        R.layout.card_claim_item,
                        parent,
                        false
                ),
                itemPresenter
        );
    }

    public void onBindViewHolder(
            final DataBoundViewHolder<ItemPresenter, ClaimItem> holder,
            final int position) {

        holder.setItem(items.get(position));
    }

    public int getItemCount() {
        return items.size();
    }

}