package com.inner.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.ProfilePictureView;
import com.helperClasses.SendNotifications;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.peek.matchup.ui2.MainActivity;
import com.peek.matchup.ui2.R;

import org.json.JSONObject;


/**
 * Created by User on 05/12/2015.
 */
public class Fragment2 extends Fragment {

    ProfilePictureView profilePictureView1;
    ProfilePictureView profilePictureView2;
    TextView textView1;
    TextView textView2;
    private final int REQUEST_CODE1 = 20;
    private final int REQUEST_CODE2 = 21;
    public static final int DATEPICKER_FRAGMENT1=20;
    public static final int DATEPICKER_FRAGMENT2=21;
    private FragmentActivity myContext;

    private String id1 = "";
    private String id2 = "";
    private String name1 = "";
    private String name2 = "";
    private Button btn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment2_layout,container,false);
        final Fragment f=this;
        final FragmentManager fragManager = myContext.getSupportFragmentManager();
        profilePictureView1=(ProfilePictureView)v.findViewById(R.id.image1);
        profilePictureView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  Intent i=new Intent(getActivity(),MyFacbookFriendsList.class);


             //   getActivity().startActivityForResult(i, REQUEST_CODE1);
                Fragment6 dialog  = new Fragment6();
                dialog.setTargetFragment(f, DATEPICKER_FRAGMENT1);
                // Show DialogFragment
                dialog .show(fragManager, "Dialog Fragment");

            }
        });
        profilePictureView2=(ProfilePictureView)v.findViewById(R.id.image2);
        profilePictureView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  Intent i=new Intent(getActivity(),MyFacbookFriendsList.class);

               // getActivity().startActivityForResult(i, REQUEST_CODE2);
                Fragment6 dialog  = new Fragment6();
                dialog.setTargetFragment(f, DATEPICKER_FRAGMENT2);
                // Show DialogFragment
                dialog .show(fragManager, "Dialog Fragment");
            }
        });
        textView1=(TextView)v.findViewById(R.id.textView2);
        textView2=(TextView)v.findViewById(R.id.textView3);


        btn = (Button) v.findViewById(R.id.btnMatchNow);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                String name = object.optString("name");
                                String id = object.optString("id");
                                String link = object.optString("link");

                                SendNotifications.newMatch(id1, id2);
                                Toast.makeText(getActivity().getApplicationContext(),"Match has been proposed succesfully",Toast.LENGTH_LONG).show();
                                ParseObject addMatch = new ParseObject("Matches");
                                addMatch.put("id1", id1);
                                addMatch.put("id2", id2);

                                addMatch.put("name1", name1);
                                addMatch.put("name2", name2);


                                addMatch.put("matcher", id);
                                addMatch.put("matcherName", name);

                                addMatch.saveInBackground();

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link");
                request.setParameters(parameters);
                request.executeAsync();



            }
        });

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode ==getActivity().RESULT_OK )
        {
            String name = data.getExtras().getString("name", "");
            String id = data.getExtras().getString("id", "");

            if (requestCode == DATEPICKER_FRAGMENT1) {
                profilePictureView1.setProfileId(id);
                textView1.setText(name);
                id1 = id;
                name1=name;
            }
            else {
                profilePictureView2.setProfileId(id);
                textView2.setText(name);
                id2 = id;
                name2=name;
            }
            if (id1.length()!=0 && id2.length()!=0)
                btn.setVisibility(View.VISIBLE);
        }


    }


}
