package com.peek.matchup.ui2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ui.ParseLoginBuilder;

import org.json.JSONObject;


public class LoginFacebook extends FragmentActivity {
    LoginFacebookFragment fragment;

    private static int RCODE = 53846;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);

        AccessToken token = AccessToken.getCurrentAccessToken();
        if (token == null) {
            ParseLoginBuilder builder = new ParseLoginBuilder(LoginFacebook.this);
            startActivityForResult(builder.build(), RCODE);
        }else if(token.isExpired()){
            if (!token.getDeclinedPermissions().isEmpty())
                token.getPermissions();
            AccessToken.refreshCurrentAccessTokenAsync();
            ParseLoginBuilder builder = new ParseLoginBuilder(LoginFacebook.this);
            startActivityForResult(builder.build(), RCODE);
        }else {
            AccessToken.refreshCurrentAccessTokenAsync();
            proceedToMainActivity();
        }

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

    private void proceedToMainActivity() {
        Log.d("Main Activity: ", "000000000000");
        final AccessToken accessToken=AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                Log.d("Main Activity: ",response.getRawResponse());
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
