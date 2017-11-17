package com.packtpub.claim.ui;

import android.content.Context;

import android.content.SharedPreferences;

import android.databinding.DataBindingUtil;

import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.packtpub.claim.R;

import com.packtpub.claim.ui.presenters.AllowanceOverviewPresenter;

import com.packtpub.claim.databinding.FragmentAllowanceOverviewBinding;

/**
 * Created by jason on 2017/11/09.
 */
public class AllowanceOverviewFragment extends Fragment {

    private FragmentAllowanceOverviewBinding binding;

    private SharedPreferences preferences;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.preferences = getContext().getSharedPreferences(
                "Allowance",
                Context.MODE_PRIVATE
        );
    }

    @Override
    public View onCreateView(
            final LayoutInflater inflater,
            final ViewGroup container,
            final Bundle savedInstanceState) {

        this.binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_allowance_overview,
                container,
                false
        );

        this.binding.setPresenter(
                new AllowanceOverviewPresenter(
                        this,
                        preferences.getInt("allowancePerDay", 150)
                )
        );

        return this.binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        preferences.edit()
                .putInt("allowancePerDay", this.binding.getPresenter().allowance.get())
                .apply();
    }
}
