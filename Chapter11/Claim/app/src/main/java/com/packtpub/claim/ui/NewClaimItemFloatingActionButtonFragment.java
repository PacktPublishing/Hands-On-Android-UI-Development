package com.packtpub.claim.ui;

import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;
import android.os.AsyncTask;

import android.support.v4.app.Fragment;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.packtpub.claim.R;
import com.packtpub.claim.ClaimApplication;
import com.packtpub.claim.CaptureClaimActivity;

import com.packtpub.claim.model.ClaimItem;

import com.packtpub.claim.model.db.ClaimDatabase;

/**
 * Created by jason on 2017/11/09.
 */
public class NewClaimItemFloatingActionButtonFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_CODE_CREATE_CLAIM_ITEM = 100;

    @Override
    public View onCreateView(
            final LayoutInflater inflater,
            final ViewGroup container,
            final Bundle savedInstanceState) {

        final View button = inflater.inflate(
                R.layout.fragment_new_claim_item_floating_action_button,
                container,
                false
        );

        button.setOnClickListener(this);

        return button;
    }

    @Override
    public void onClick(final View view) {
        startActivityForResult(
                new Intent(getContext(), CaptureClaimActivity.class),
                REQUEST_CODE_CREATE_CLAIM_ITEM
        );
    }

    public void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {

        if (requestCode != REQUEST_CODE_CREATE_CLAIM_ITEM
                || resultCode != Activity.RESULT_OK
                || data == null) {

            return;
        }

        final ClaimItem claimItem = data.getParcelableExtra(
                CaptureClaimActivity.EXTRA_CLAIM_ITEM
        );

        if (claimItem.isValid()) {
            final ClaimDatabase database = ClaimApplication.getClaimDatabase();
            AsyncTask.SERIAL_EXECUTOR.execute(database.createClaimItemTask(claimItem));
        }
    }
}
