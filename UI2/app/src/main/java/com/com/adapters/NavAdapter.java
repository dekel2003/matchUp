package com.com.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.models.NavIteam;
import com.peek.matchup.ui2.R;

import java.util.List;

/**
 * Created by User on 06/12/2015.
 */
public class NavAdapter extends ArrayAdapter<NavIteam> {
    Context context;
    int resleyout;
    List<NavIteam> navIteamList;

    public NavAdapter(Context context, int resleyout,List<NavIteam> navIteamList) {
        super(context, resleyout, navIteamList);
        this.context=context;
        this.resleyout=resleyout;
        this.navIteamList=navIteamList;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=View.inflate(context,resleyout,null);

        TextView title=(TextView) v.findViewById(R.id.title_txt);
        TextView subTitle=(TextView) v.findViewById(R.id.subTitle);
        ImageView navIcon=(ImageView)v.findViewById(R.id.imageView);

        NavIteam navIteam=navIteamList.get(position);

        title.setText(navIteam.getTitle());
        subTitle.setText(navIteam.getSubTitle());
        navIcon.setImageResource(navIteam.getResIcon());

        return v;

    }


}
