package com.inner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.peek.matchup.ui2.R;
import com.squareup.picasso.Picasso;


/**
 * Created by User on 05/12/2015.
 */
public class PicFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.pic_fragment_layout,container,false);
        Bundle bundle = this.getArguments();
        String PicPath =bundle.getString("PicPath", "");
        ImageView imageView=(ImageView)v.findViewById(R.id.image);
        Picasso.with(getContext()).load(PicPath).resize(400, 400)
                .centerCrop().into(imageView);
        return  v;
    }
}
