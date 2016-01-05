package com.helperClasses;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.parse.ParseException;
import com.parse.ui.ParseLoginBuilder;
import com.peek.matchup.ui2.LoginFacebook;

import static android.support.v4.app.ActivityCompat.startActivity;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Dekel on 1/5/2016.
 */
public class ParseErrorHandler {
    public static void handleParseError(Context context, ParseException e) {
        switch (e.getCode()) {
            case ParseException.INVALID_SESSION_TOKEN: handleInvalidSessionToken(context);
                break;


        }
    }

    private static void handleInvalidSessionToken(final Context context) {
        //--------------------------------------
        // Option 1: Show a message asking the user to log out and log back in.
        //--------------------------------------
        // If the user needs to finish what they were doing, they have the opportunity to do so.
        //
        new AlertDialog.Builder(getApplicationContext())
                .setMessage("Session is no longer valid, Logging out.")
                .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(),LoginFacebook.class);
                context.startActivity(intent);

//                ParseLoginBuilder builder = new ParseLoginBuilder(getApplicationContext());
//                startActivityForResult(builder.build(), LoginFacebook.RCODE);
//                startActivityForResult(new ParseLoginBuilder(getActivity()).build(), 0);
            }
        }).create().show();

        //--------------------------------------
        // Option #2: Show login screen so user can re-authenticate.
        //--------------------------------------
        // You may want this if the logout button could be inaccessible in the UI.
        //
        // startActivityForResult(new ParseLoginBuilder(getActivity()).build(), 0);
    }

}