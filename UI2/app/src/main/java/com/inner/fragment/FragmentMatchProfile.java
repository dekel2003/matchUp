package com.inner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragmentmatchprofile_layout, container, false);
        description=(TextView) v.findViewById(R.id.textView);
        Bundle bundle = this.getArguments();
        String idmyMatch =bundle.getString("idmyMatch", "");
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
}
