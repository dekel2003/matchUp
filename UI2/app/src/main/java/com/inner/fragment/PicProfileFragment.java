package com.inner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.com.fragments.Profile;
import com.facebook.login.widget.ProfilePictureView;
import com.peek.matchup.ui2.R;


/**
 * Created by User on 05/12/2015.
 */
public class PicProfileFragment extends Fragment {
    ProfilePictureView profilePictureView;
    int mode;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mode=0;
        View v=inflater.inflate(R.layout.pic_profle_fragment_layout,container,false);
        Bundle bundle = this.getArguments();
        String idmyMatch =bundle.getString("idmyMatch", "");
        final Animation animZin;
        final Animation animOut;
        animZin= AnimationUtils.loadAnimation(getActivity(), R.anim.zoomin);
        animOut= AnimationUtils.loadAnimation(getActivity(), R.anim.zoomout);
        profilePictureView=(ProfilePictureView) v.findViewById(R.id.image);
        profilePictureView.setProfileId(idmyMatch);
        profilePictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == 0)
                    profilePictureView.startAnimation(animZin);
                else
                    profilePictureView.startAnimation(animOut);
                mode = (mode + 1) % 2;
            }
        });
        return  v;
    }
}
