package com.packtpub.claim;

import android.content.Intent;

import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;

import android.text.TextUtils;

import android.view.MenuItem;

import android.view.View;

import android.widget.EditText;

import com.packtpub.claim.model.ClaimItem;
import com.packtpub.claim.widget.DatePickerLayout;
import com.packtpub.claim.ui.CategoryPickerFragment;
import com.packtpub.claim.ui.attachments.AttachmentPagerFragment;

public class CaptureClaimActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_CLAIM_ITEM = "com.packtpub.claim.extras.CLAIM_ITEM";
    private static final String KEY_CLAIM_ITEM = "com.packtpub.claim.ClaimItem";

    private EditText description;
    private EditText amount;

    private DatePickerLayout selectedDate;
    private CategoryPickerFragment categories;
    private AttachmentPagerFragment attachments;

    private ClaimItem claimItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_claim);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton attach = (FloatingActionButton) findViewById(R.id.attach);
        attach.setOnClickListener(this);

        selectedDate = (DatePickerLayout) findViewById(R.id.date);

        description = (EditText) findViewById(R.id.description);
        amount = (EditText) findViewById(R.id.amount);
        selectedDate = (DatePickerLayout) findViewById(R.id.date);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        attachments = (AttachmentPagerFragment) fragmentManager.findFragmentById(R.id.attachments);
        categories = (CategoryPickerFragment) fragmentManager.findFragmentById(R.id.categories);

        if (savedInstanceState != null) {
            claimItem = savedInstanceState.getParcelable(KEY_CLAIM_ITEM);
        } else if (getIntent().hasExtra(EXTRA_CLAIM_ITEM)) {
            claimItem = getIntent().getParcelableExtra(EXTRA_CLAIM_ITEM);
        }

        if (claimItem == null) {
            claimItem = new ClaimItem();
        } else {
            description.setText(claimItem.getDescription());
            amount.setText(String.format("%f", claimItem.getAmount()));
            selectedDate.setDate(claimItem.getTimestamp());
        }

        attachments.setClaimItem(claimItem);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.attach:
                attachments.onAttachClick();
                break;
        }
    }

    void captureClaimItem() {
        claimItem.setDescription(description.getText().toString());
        if (!TextUtils.isEmpty(amount.getText())) {
            claimItem.setAmount(Double.parseDouble(amount.getText().toString()));
        }
        claimItem.setTimestamp(selectedDate.getDate());
        claimItem.setCategory(categories.getSelectedCategory());
    }

    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        captureClaimItem(); // make sure the ClaimItem is up-to-date
        outState.putParcelable(KEY_CLAIM_ITEM, claimItem);
    }

    public void finish() {
        captureClaimItem();
        setResult(RESULT_OK, new Intent().putExtra(EXTRA_CLAIM_ITEM, claimItem));
        super.finish();
    }

}