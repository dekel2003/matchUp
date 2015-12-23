package com.peek.matchup.ui2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.parse.ui.ParseLoginBuilder;

import org.json.JSONObject;


public class LoginFacebook extends FragmentActivity {
    LoginFacebookFragment fragment;

    private static int RCODE = 53846;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AccessToken.getCurrentAccessToken() != null){
            proceedToMainActivity();
        }else {
            ParseLoginBuilder builder = new ParseLoginBuilder(LoginFacebook.this);
            startActivityForResult(builder.build(), RCODE);
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
        AccessToken accessToken=AccessToken.getCurrentAccessToken();
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
