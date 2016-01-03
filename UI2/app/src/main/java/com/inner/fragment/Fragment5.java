package com.inner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.peek.matchup.ui2.R;


/**
 * Created by User on 05/12/2015.
 */


public class Fragment5 extends Fragment {
    ProfilePictureView profilePictureViewMacher;
    TextView nameMacher;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment5_layout,container,false);
        Bundle bundle = this.getArguments();
        String macherName =bundle.getString("namemacher", "");
        String idMacher = bundle.getString("idmacher", "");
        String rec = bundle.getString("rec", "");
        TextView recomntion=(TextView) v.findViewById(R.id.rec);
        recomntion.setText(rec);
        profilePictureViewMacher=(ProfilePictureView)v.findViewById(R.id.imageMatcher);
        nameMacher=(TextView) v.findViewById(R.id.machername);
        profilePictureViewMacher.setProfileId(idMacher);
        nameMacher.setText(macherName);

        return v;
    }
}
