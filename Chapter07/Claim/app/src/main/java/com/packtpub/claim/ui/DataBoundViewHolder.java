package com.packtpub.claim.ui;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import com.android.databinding.library.baseAdapters.BR;

/**
 * Created by jason on 2017/11/09.
 */
public class DataBoundViewHolder<P, I> extends RecyclerView.ViewHolder {

    private final ViewDataBinding binding;

    public DataBoundViewHolder(final ViewDataBinding binding, final P presenter) {
        super(binding.getRoot());
        this.binding = binding;
        this.binding.setVariable(BR.presenter, presenter);
    }

    public void setItem(final I item) {
        binding.setVariable(BR.item, item);
    }

    public void setPresenter(final P presenter) {
        binding.setVariable(BR.presenter, presenter);
    }

}