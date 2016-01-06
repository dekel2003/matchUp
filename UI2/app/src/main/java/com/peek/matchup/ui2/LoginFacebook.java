package com.peek.matchup.ui2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseSession;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginActivity;
import com.parse.ui.ParseLoginBuilder;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;

import static com.facebook.FacebookSdk.getApplicationContext;


public class LoginFacebook extends FragmentActivity {
    LoginFacebookFragment fragment;
    AccessToken token;

    public static int RCODE = 53846;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);

        token = AccessToken.getCurrentAccessToken();


        if (token!=null)
            proceedToMainActivity();
        else
            gotoLoginScreen();


//        if (!token.getDeclinedPermissions().isEmpty())
//            Log.d("Main Activity: ", "declined permission - log in again.");
//            gotoLoginScreen();
//        if(!ParseFacebookUtils.isLinked(ParseUser.getCurrentUser())) {
//            Log.d("Main Activity: ", "unlinked account - log out.");
//            ParseUser.logOut();
//            gotoLoginScreen();
//        }else {
//            proceedToMainActivity();
//        }

        if(savedInstanceState==null)
        {
//            fragment=new LoginFacebookFragment();
//            getSupportFragmentManager().beginTransaction().add(android.R.id.content,fragment).commit();

        }
        else
        {
//            fragment=(LoginFacebookFragment) getSupportFragmentManager().findFragmentById(android.R.id.content);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RCODE && resultCode == RESULT_OK){
            proceedToMainActivity();
        }else{
            finish();
        }
    }

    private void gotoLoginScreen(){
        ParseLoginBuilder builder = new ParseLoginBuilder(LoginFacebook.this);
        startActivityForResult(builder.build(), RCODE);
    }

    private void proceedToMainActivity() {
        Log.d("Main Activity: ", "000000000000");
        AccessToken token = AccessToken.getCurrentAccessToken();

        if (!token.getDeclinedPermissions().isEmpty()) {
            Log.d("Main Activity: ", "declined permission - log in again.");
            gotoLoginScreen();
        }
        if(!ParseFacebookUtils.isLinked(ParseUser.getCurrentUser())) {
            Log.d("Main Activity: ", "unlinked account - log out.");
            ParseUser.logOut();
            gotoLoginScreen();
        }
        final AccessToken accessToken=AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (response.getError()!=null){
                    Log.d("Main Activity: ", response.getError().getErrorMessage());
                    AccessToken.refreshCurrentAccessTokenAsync();
                    ParseLoginBuilder builder = new ParseLoginBuilder(LoginFacebook.this);
                    startActivityForResult(builder.build(), RCODE);
                    return;
                }
                Log.d("Main Activity: ", "connected with Token: " + accessToken.getToken());
                String name=object.optString("name");
                String id=object.optString("id");
                Intent i=new Intent(LoginFacebook.this,MainActivity.class);
                i.putExtra("name",name);
                i.putExtra("id", id);
                i.putExtra("birthday", object.optString("birthday"));
                i.putExtra("gender", object.optString("gender"));


                ParseInstallation pi = ParseInstallation.getCurrentInstallation();
                pi.put("FacebookID",id);
                pi.saveInBackground();

                startActivity(i);
                finish();
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,birthday,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }




}
