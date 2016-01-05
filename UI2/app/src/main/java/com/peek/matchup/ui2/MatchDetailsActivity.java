package com.peek.matchup.ui2;

import android.app.Activity;

import android.app.FragmentTransaction;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.com.fragments.MatchDetailes;

public class MatchDetailsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_details);
        Bundle extras = getIntent().getExtras();

        Bundle bundle = new Bundle();
        bundle.putString("namemacher", extras.getString("namemacher"));
        bundle.putString("idmacher", extras.getString("idmacher"));
        bundle.putString("idmyMatch", extras.getString("idmyMatch"));
        bundle.putString("rec", extras.getString("rec"));
        bundle.putString("matchId", extras.getString("matchId"));
        bundle.putString("side", extras.getString("side"));
           Fragment mFragment = new MatchDetailes();
        mFragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();

                    fragmentManager.beginTransaction()
                    .replace(R.id.container, mFragment).commit();

         }


}
