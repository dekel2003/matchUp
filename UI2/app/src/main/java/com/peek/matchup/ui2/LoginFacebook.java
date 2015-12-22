package com.peek.matchup.ui2;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.parse.Parse;
import com.parse.ParseInstallation;

public class LoginFacebook extends FragmentActivity {
    LoginFacebookFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.enableLocalDatastore(this.getApplicationContext());
        Parse.initialize(this, "ZrCnAe33kupfzOuJ7sncBXMOPYFXWJpDqIqWW2nb", "VLpNsGsoKEKvjQSWu0cdLn9NmAz889FfLZ6CfPFy");
        ParseInstallation.getCurrentInstallation().saveInBackground();

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
