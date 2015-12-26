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


public class Fragment5 extends DialogFragment {
    ProfilePictureView profilePictureViewMacher;
    TextView nameMacher;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment5_layout,container,false);
        getDialog().setTitle("Your Match");
        String macherName = getArguments().getString("namemacher");
        String idMacher = getArguments().getString("idmacher");
        profilePictureViewMacher=(ProfilePictureView)v.findViewById(R.id.imageMatcher);
        nameMacher=(TextView) v.findViewById(R.id.machername);
        profilePictureViewMacher.setProfileId(idMacher);
        nameMacher.setText(macherName);

        return v;
    }
}
