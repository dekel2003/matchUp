package com.com.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.models.NavIteam;
import com.models.NavItemFacebook;
import com.peek.matchup.ui2.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

/**
 * Created by User on 06/12/2015.
 */
public class NavAdapterFacebook extends ArrayAdapter<NavItemFacebook> {
    Context context;
    int resleyout;
    List<NavItemFacebook> navIteamList;

    public NavAdapterFacebook(Context context, int resleyout, List<NavItemFacebook> navIteamList) {
        super(context, resleyout, navIteamList);
        this.context=context;
        this.resleyout=resleyout;
        this.navIteamList=navIteamList;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=View.inflate(context,resleyout,null);

        TextView title=(TextView) v.findViewById(R.id.title_txt);
        //ProfilePictureView navIcon=(ProfilePictureView)v.findViewById(R.id.imageView);
        ImageView navIcon=(ImageView)v.findViewById(R.id.imageView);

        NavItemFacebook navItemFacebook=navIteamList.get(position);

        title.setText(navItemFacebook.getTitle());
       // navIcon.setProfileId(navItemFacebook.getId());


        String imgUrl = "https://graph.facebook.com/" + navItemFacebook.getId() + "/picture?type=large";
        Picasso.with(context).load(imgUrl).resize(180, 180)
                .centerCrop().into(navIcon);







        return v;

    }



}
