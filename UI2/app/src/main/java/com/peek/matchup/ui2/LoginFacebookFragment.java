package com.peek.matchup.ui2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.Parse;
import com.parse.ParseObject;

import org.json.JSONObject;


public class LoginFacebookFragment extends Fragment {

    private CallbackManager callbackManager;
    String name;
    String id;





    public LoginFacebookFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager=CallbackManager.Factory.create();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       View v=inflater.inflate(R.layout.fragment_login, container, false);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        AccessToken accessToken=AccessToken.getCurrentAccessToken();
        if(accessToken!=null)
            getInfoAndMOveOn(accessToken);
        else
            loginButton.setVisibility(View.VISIBLE);


    }


    private void getInfoAndMOveOn(final AccessToken accessToken)
    {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        name=object.optString("name");
                        id=object.optString("id");
                        String link=object.optString("link");
                        //Log.d("name",name);
                        //Log.d("id",id);
                        //Log.d("link", link);
                        //ProfilePictureView profilePictureView=(ProfilePictureView) view.findViewById(R.id.pic);
                        // profilePictureView.setProfileId(id);
                        Intent i=new Intent(getActivity(),MainActivity.class);
                        i.putExtra("name",name);
                        i.putExtra("id", id);
                        startActivity(i);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();
    }

     LoginButton loginButton;
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setVisibility(View.INVISIBLE);
        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {


            @Override
            public void onSuccess(LoginResult loginResult) {
                loginButton.setVisibility(View.INVISIBLE);

                // Parse.enableLocalDatastore(getActivity().getApplicationContext());
                // Parse.initialize(getActivity().getApplicationContext());

                ParseObject user = new ParseObject("Users");
                user.put("FacebookID", id);
                user.put("Name", name);
                user.saveInBackground();


               //getInfoAndMOveOn(AccessToken.getCurrentAccessToken());

         /*   if(Profile.getCurrentProfile() == null) {
                mProfileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                        detiles.setText("facebook - profile" + profile2.getFirstName());

                        mProfileTracker.stopTracking();
                    }
                };
                mProfileTracker.startTracking();
            }
            else {
                Profile profile = Profile.getCurrentProfile();
                detiles.setText("facebook - profile"+ profile.getFirstName());

            }*/
            }




            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);

    }

}