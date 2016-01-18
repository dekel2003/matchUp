package com.peek.matchup.ui2;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;

import net.danlew.android.joda.JodaTimeAndroid;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by User on 08/12/2015.
 */
public class MyAppliction extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        printHashkey();

        JodaTimeAndroid.init(this);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "ZrCnAe33kupfzOuJ7sncBXMOPYFXWJpDqIqWW2nb", "VLpNsGsoKEKvjQSWu0cdLn9NmAz889FfLZ6CfPFy");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParseFacebookUtils.initialize(this);
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
    }

    public void printHashkey()
    {

        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    this.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("com", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}
