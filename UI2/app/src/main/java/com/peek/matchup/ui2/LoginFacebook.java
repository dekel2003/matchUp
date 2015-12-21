package com.peek.matchup.ui2;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.Parse;

import org.json.JSONObject;

public class LoginFacebook extends FragmentActivity {
    LoginFacebookFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if(savedInstanceState==null)
        {
            fragment=new LoginFacebookFragment();

            getSupportFragmentManager().beginTransaction().add(android.R.id.content,fragment).commit();

        }
        else
        {
            fragment=(LoginFacebookFragment) getSupportFragmentManager().findFragmentById(android.R.id.content);
        }

    }



}
