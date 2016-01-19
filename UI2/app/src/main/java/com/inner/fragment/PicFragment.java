package com.inner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.peek.matchup.ui2.R;
import com.squareup.picasso.Picasso;


/**
 * Created by User on 05/12/2015.
 */
public class PicFragment extends Fragment {
    int mode;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mode=0;
        View v=inflater.inflate(R.layout.pic_fragment_layout, container, false);
        Bundle bundle = this.getArguments();
        String PicPath =bundle.getString("PicPath", "");
        final Animation animZin;
        final Animation animOut;
        animZin= AnimationUtils.loadAnimation(getActivity(), R.anim.zoomin);
        animOut= AnimationUtils.loadAnimation(getActivity(), R.anim.zoomout);
        final ImageView imageView=(ImageView)v.findViewById(R.id.image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode==0)
                    imageView.startAnimation(animZin);
                else
                    imageView.startAnimation(animOut);
                mode=(mode+1)%2;
            }
        });
        Picasso.with(getContext()).load(PicPath).resize(400, 400)
                .centerCrop().into(imageView);
        return  v;
    }
}
