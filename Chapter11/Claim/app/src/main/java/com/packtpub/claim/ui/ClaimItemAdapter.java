package com.packtpub.claim.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LifecycleOwner;

import android.content.Context;

import android.databinding.DataBindingUtil;

import android.support.v4.util.Pair;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.packtpub.claim.R;

import com.packtpub.claim.model.ClaimItem;

import com.packtpub.claim.ui.presenters.ItemPresenter;

import com.packtpub.claim.util.ActionCommand;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Collections;

/**
 * Created by jason on 2017/11/09.
 */
public class ClaimItemAdapter extends RecyclerView.Adapter<DataBoundViewHolder<ItemPresenter, ClaimItem>> {
    private final UpdateDisplayListCommand updateCommand = new UpdateDisplayListCommand();
    private final CreateDisplayListCommand createDisplayListCommand = new CreateDisplayListCommand();

    private final LayoutInflater layoutInflater;
    private final ItemPresenter itemPresenter;

    private List<DisplayItem> items = Collections.emptyList();

    public ClaimItemAdapter(
            final Context context,
            final LifecycleOwner owner,
            final LiveData<List<ClaimItem>> liveItems) {

        this.layoutInflater = LayoutInflater.from(context);
        this.itemPresenter = new ItemPresenter(context);

        liveItems.observe(owner, new Observer<List<ClaimItem>>() {
            @Override
            public void onChanged(final List<ClaimItem> claimItems) {
                if (!items.isEmpty()) {
                    updateCommand.exec(Pair.create(items, claimItems));
                } else {
                    createDisplayListCommand.exec(claimItems);
                }
            }
        });
    }

    public DataBoundViewHolder<ItemPresenter, ClaimItem> onCreateViewHolder(
            final ViewGroup parent, final int viewType) {

        return new DataBoundViewHolder<>(
                DataBindingUtil.inflate(
                        layoutInflater,
                        viewType,
                        parent,
                        false
                ),
                itemPresenter
        );
    }

    public void onBindViewHolder(
            final DataBoundViewHolder<ItemPresenter, ClaimItem> holder,
            final int position) {
        items.get(position).bindItem(holder);

    }

    public int getItemCount() {
        return items.size();
    }


    @Override
    public int getItemViewType(final int position) {
        return items.get(position).layout;
    }

    private class CreateDisplayListCommand extends ActionCommand<List<ClaimItem>, List<DisplayItem>> {
        boolean isDividerRequired(final ClaimItem item1, final ClaimItem item2) {
            final Calendar c1 = Calendar.getInstance();
            final Calendar c2 = Calendar.getInstance();

            c1.setTime(item1.getTimestamp());
            c2.setTime(item2.getTimestamp());

            return c1.get(Calendar.DAY_OF_YEAR) != c2.get(Calendar.DAY_OF_YEAR)
                    || c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR);
        }

        @Override
        public List<DisplayItem> onBackground(
                final List<ClaimItem> claimItems)
                throws Exception {

            final List<DisplayItem> output = new ArrayList<>();

            for (int i = 0; i < claimItems.size(); i++) {
                final ClaimItem item = claimItems.get(i);
                output.add(new DisplayItem(R.layout.card_claim_item, item));

                if (i + 1 < claimItems.size() // not the last item
                        && isDividerRequired(item, claimItems.get(i + 1))) {

                    output.add(new DisplayItem(R.layout.widget_divider, null));
                }
            }

            return output;
        }

        @Override
        public void onForeground(final List<DisplayItem> value) {
            ClaimItemAdapter.this.items = value;
            notifyDataSetChanged();
        }
    }

    private class UpdateDisplayListCommand
            extends ActionCommand<
            Pair<List<DisplayItem>, List<ClaimItem>>,
            Pair<List<DisplayItem>, DiffUtil.DiffResult>
            > {

        @Override
        public Pair<List<DisplayItem>, DiffUtil.DiffResult> onBackground(
                final Pair<List<DisplayItem>, List<ClaimItem>> args)
                throws Exception {

            final List<DisplayItem> oldDisplay = args.first;
            final List<DisplayItem> newDisplay = createDisplayListCommand.onBackground(args.second);

            final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {

                @Override
                public int getOldListSize() {
                    return oldDisplay.size();
                }

                @Override
                public int getNewListSize() {
                    return newDisplay.size();
                }

                @Override
                public boolean areItemsTheSame(final int oldItemPosition, final int newItemPosition) {
                    final DisplayItem oldItem = oldDisplay.get(oldItemPosition);
                    final DisplayItem newItem = newDisplay.get(newItemPosition);

                    if (oldItem.layout != newItem.layout) {
                        return false;
                    }

                    switch (newItem.layout) {
                        case R.layout.card_claim_item:
                            final ClaimItem oldClaimItem = (ClaimItem) oldItem.value;
                            final ClaimItem newClaimItem = (ClaimItem) newItem.value;
                            return oldClaimItem.id == newClaimItem.id;
                        case R.layout.widget_divider:
                            return true;
                    }

                    return false;
                }

                @Override
                public boolean areContentsTheSame(final int oldItemPosition, final int newItemPosition) {
                    final DisplayItem oldItem = oldDisplay.get(oldItemPosition);
                    final DisplayItem newItem = newDisplay.get(newItemPosition);

                    switch (newItem.layout) {
                        case R.layout.card_claim_item:
                            final ClaimItem oldClaimItem = (ClaimItem) oldItem.value;
                            final ClaimItem newClaimItem = (ClaimItem) newItem.value;
                            return oldClaimItem.equals(newClaimItem);
                        case R.layout.widget_divider:
                            return true;
                    }

                    return false;
                }
            });

            return Pair.create(newDisplay, result);
        }

        @Override
        public void onForeground(final Pair<List<DisplayItem>, DiffUtil.DiffResult> value) {
            ClaimItemAdapter.this.items = value.first;
            value.second.dispatchUpdatesTo(ClaimItemAdapter.this);
        }
    }

}