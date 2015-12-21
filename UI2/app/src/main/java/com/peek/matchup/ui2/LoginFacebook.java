package com.peek.matchup.ui2;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

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
