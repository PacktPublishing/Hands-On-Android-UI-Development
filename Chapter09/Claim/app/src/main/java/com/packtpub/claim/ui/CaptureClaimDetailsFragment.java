package com.packtpub.claim.ui;


import android.os.Bundle;

import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;

import android.text.TextUtils;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.EditText;

import com.packtpub.claim.R;
import com.packtpub.claim.model.ClaimItem;
import com.packtpub.claim.widget.DatePickerLayout;


/**
 * Created by jason on 2017/11/07.
 */
public class CaptureClaimDetailsFragment extends Fragment {

    private EditText description;

    private EditText amount;

    private DatePickerLayout selectedDate;

    private ClaimItem claimItem;

    public void setClaimItem(final ClaimItem claimItem) {
        this.claimItem = claimItem;
    }

    @Nullable
    @Override
    public View onCreateView(
            final LayoutInflater inflater,
            final @Nullable ViewGroup container,
            final @Nullable Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_capture_claim_details, container, false);

        description = (EditText) root.findViewById(R.id.description);
        amount = (EditText) root.findViewById(R.id.amount);
        selectedDate = (DatePickerLayout) root.findViewById(R.id.date);

        return root;
    }

    public void captureClaimItem() {
        claimItem.setDescription(description.getText().toString());

        if (!TextUtils.isEmpty(amount.getText())) {
            claimItem.setAmount(Double.parseDouble(amount.getText().toString()));
        }

        claimItem.setTimestamp(selectedDate.getDate());
    }
}
