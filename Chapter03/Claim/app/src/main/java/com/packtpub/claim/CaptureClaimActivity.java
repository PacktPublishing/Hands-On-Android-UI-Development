package com.packtpub.claim;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.packtpub.claim.ui.DatePickerWrapper;
import com.packtpub.claim.ui.IconPickerWrapper;

public class CaptureClaimActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_ATTACH_FILE = 1;
    private static final int REQUEST_ATTACH_PERMISSION = 1001;

    private DatePickerWrapper selectedDate;
    private RadioGroup categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_capture_claim);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton attach = (FloatingActionButton) findViewById(R.id.attach);
        attach.setOnClickListener(this);

        selectedDate = new DatePickerWrapper((TextView) findViewById(R.id.date));

        categories = (RadioGroup) findViewById(R.id.categories);
        categories.setOnCheckedChangeListener(
                new IconPickerWrapper((TextView) findViewById(R.id.selected_category))
        );

        categories.check(R.id.other);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.attach:
                onAttachClick();
                break;
        }
    }

    public void onAttachClick() {
        final int permissionStatus = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
        );

        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_ATTACH_PERMISSION
            );

            return;
        }

        final Intent attach = new Intent(Intent.ACTION_GET_CONTENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
                .setType("*/*");

        startActivityForResult(attach, REQUEST_ATTACH_FILE);
    }

    @Override
    public void onRequestPermissionsResult(
            final int requestCode,
            final String[] permissions,
            final int[] grantResults) {

        switch (requestCode) {
            case REQUEST_ATTACH_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onAttachClick();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {

        switch (requestCode) {
            case REQUEST_ATTACH_FILE:
                onAttachFileResult(resultCode, data);
                break;
        }
    }

    public void onAttachFileResult(final int resultCode, final Intent data) {
        if (resultCode != RESULT_OK || data == null || data.getData() == null) {
            return;
        }

        Toast.makeText(this, data.getDataString(), Toast.LENGTH_SHORT).show();
    }
}
