package com.peek.matchup.ui2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.facebook.AccessToken;
import com.parse.Parse;
import com.parse.ParseInstallation;

public class InitApp extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_app);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "ZrCnAe33kupfzOuJ7sncBXMOPYFXWJpDqIqWW2nb", "VLpNsGsoKEKvjQSWu0cdLn9NmAz889FfLZ6CfPFy");
        ParseInstallation.getCurrentInstallation().saveInBackground();



        Intent intent = new Intent(this, LoginFacebook.class);
        startActivity(intent);
        finish();
    }
}
