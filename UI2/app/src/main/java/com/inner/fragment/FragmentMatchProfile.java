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
import com.peek.matchup.ui2.R;


/**
 * Created by User on 05/12/2015.
 */


public class FragmentMatchProfile extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragmentmatchprofile_layout, container, false);
        Bundle bundle = this.getArguments();
        //String idmyMatch =bundle.getString("idmyMatch", "");
        Fragment fragment = new MatchPicSlide();

        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();


        return v;
    }
}
