package com.packtpub.claim;

import android.os.Bundle;

import android.support.v7.widget.RecyclerView;

import android.support.v7.app.AppCompatActivity;

import com.packtpub.claim.ui.ClaimItemAdapter;

/**
 * Created by jason on 2017/11/09.
 */
public class OverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        final RecyclerView claimItems = findViewById(R.id.claim_items);
        claimItems.setAdapter(
                new ClaimItemAdapter(
                        this, this, // both the Context, and LifecycleOwner are the OverviewActivity
                        ClaimApplication.getClaimDatabase().claimItemDao().selectAll()
                )
        );
    }

}