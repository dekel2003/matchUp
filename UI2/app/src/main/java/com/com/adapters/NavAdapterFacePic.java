package com.com.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.com.fragments.DownloadImageTask;
import com.facebook.login.widget.ProfilePictureView;
import com.models.NavItemFacebook;
import com.peek.matchup.ui2.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by User on 06/12/2015.
 */
public class NavAdapterFacePic extends ArrayAdapter<NavItemFacebook> {
    Context context;
    int resleyout;
    List<NavItemFacebook> navIteamList;

    public NavAdapterFacePic(Context context, int resleyout, List<NavItemFacebook> navIteamList) {
        super(context, resleyout, navIteamList);
        this.context=context;
        this.resleyout=resleyout;
        this.navIteamList=navIteamList;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=View.inflate(context,resleyout,null);

      //  TextView title=(TextView) v.findViewById(R.id.title_txt);
        ImageView navIcon=(ImageView)v.findViewById(R.id.imageView);

        NavItemFacebook navItemFacebook=navIteamList.get(position);


        //  new DownloadImageTask(navIcon)
          //      .execute(navItemFacebook.getId());
        Picasso.with(context).load(navItemFacebook.getId()).resize(200, 200)
                .centerCrop().into(navIcon);





        return v;

    }



}
