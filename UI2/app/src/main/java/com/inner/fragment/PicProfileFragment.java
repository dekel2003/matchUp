package com.inner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.com.fragments.Profile;
import com.facebook.login.widget.ProfilePictureView;
import com.peek.matchup.ui2.R;


/**
 * Created by User on 05/12/2015.
 */
public class PicProfileFragment extends Fragment {
    ProfilePictureView profilePictureView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.pic_profle_fragment_layout,container,false);
        Bundle bundle = this.getArguments();
        String idmyMatch =bundle.getString("idmyMatch", "");
        profilePictureView=(ProfilePictureView) v.findViewById(R.id.image);
        profilePictureView.setProfileId(idmyMatch);
        return  v;
    }
}
