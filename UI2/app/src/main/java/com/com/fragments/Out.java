package com.com.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.login.LoginManager;
import com.peek.matchup.ui2.R;

/**
 * Created by User on 06/12/2015.
 */
public class Out extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_out,container,false);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        LoginManager.getInstance().logOut();
        getActivity().finish();
        /*
        Intent i=new Intent(getActivity(),LoginFacebook.class);
        ParseUser.logOut();
        startActivity(i);
        getActivity().finish();
        */
    }
}
