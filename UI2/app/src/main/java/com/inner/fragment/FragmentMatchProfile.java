package com.inner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.com.fragments.MatchPicSlide;
import com.facebook.login.widget.ProfilePictureView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.peek.matchup.ui2.R;

import java.util.List;


/**
 * Created by User on 05/12/2015.
 */


public class FragmentMatchProfile extends Fragment {
    TextView description;
    Button accept;
    Button decline;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragmentmatchprofile_layout, container, false);
        final Bundle bundle = this.getArguments();
        String idmyMatch =bundle.getString("idmyMatch", "");

        description=(TextView) v.findViewById(R.id.textView);
        accept=(Button)v.findViewById(R.id.btnY);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAns(bundle.getString("matchId", ""),bundle.getString("side", ""));
                getActivity().finish();

            }
        });
        decline=(Button)v.findViewById(R.id.btnN);
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMatch(bundle.getString("matchId", ""));
                getActivity().finish();
            }
        });



        ParseQuery<ParseObject> query = ParseQuery.getQuery("profiles");
        query.whereEqualTo("facebookId", idmyMatch);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> nameList, ParseException e) {
                if (e == null && nameList!=null) {

                    for (ParseObject Obj : nameList) {
                      description.setText(Obj.get("aboutme").toString());
                    }

                } else {

                }
            }
        });



        Fragment fragment = new MatchPicSlide();

        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();


        return v;
    }

    void saveAns(String matchid,final String side)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Matches");
        query.whereEqualTo("objectId", matchid);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    ParseObject person = list.get(0);
                    if(side.compareTo("1")==0)
                        person.put("approve1", "1");
                    if(side.compareTo("2")==0)
                        person.put("approve2", "1");
                    person.saveInBackground();
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

    }

    void deleteMatch(String matchid)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Matches");
        query.whereEqualTo("objectId", matchid);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> invites, ParseException e) {
                if (e == null) {
                    // iterate over all messages and delete them
                    for(ParseObject invite : invites)
                    {
                        invite.deleteInBackground();
                    }
                } else {
                    //Handle condition here
                }
            }
        });

    }
}
