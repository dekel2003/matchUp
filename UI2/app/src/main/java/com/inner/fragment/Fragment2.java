package com.inner.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.com.adapters.NavAdapter;
import com.com.adapters.NavAdapterFacebook;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.login.widget.ProfilePictureView;
import com.models.NavIteam;
import com.models.NavItemFacebook;
import com.peek.matchup.ui2.MainActivity;
import com.peek.matchup.ui2.MyFacbookFriendsList;
import com.peek.matchup.ui2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


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
            }
            else {
                profilePictureView2.setProfileId(id);
                textView2.setText(name);
            }


        }


    }


}
