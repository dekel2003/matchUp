package com.inner.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.peek.matchup.ui2.R;


/**
 * Created by User on 05/12/2015.
 */
public class Fragment1 extends Fragment {

    public Fragment1(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment1_layout,container,false);

//        TextView tv = (TextView) container.findViewById(R.id.textView);
        TextView tv = (TextView) v.findViewById(R.id.textView);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),
                        R.drawable.dekel);
                ImageView iv = (ImageView) container.findViewById(R.id.example_image_view);
                iv.setImageBitmap(icon);

                ImageView iv2 = (ImageView) getActivity().findViewById(R.id.icon);
                iv2.setImageBitmap(icon);
            }


        });


        return  v;
    }

}
