package com.com.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;

import com.com.adapters.MyFragmentPagerAdaper;
import com.inner.fragment.Fragment1;
import com.inner.fragment.Fragment5;
import com.inner.fragment.FragmentMatchProfile;
import com.inner.fragment.PicFragment;
import com.inner.fragment.PicProfileFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.peek.matchup.ui2.FakeContant;
import com.peek.matchup.ui2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 06/12/2015.
 */
public class MatchPicSlide extends Fragment implements ViewPager.OnPageChangeListener{

    ViewPager viewPager;
    TabHost tabHost;
    MyFragmentPagerAdaper myFragmentPagerAdaper;
    int i=0;
    View v;
    List<String> pics;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.slide_view_pager_layout, container, false);
        pics=new ArrayList<String>();
        Bundle bundle = this.getArguments();
        String idmyMatch =bundle.getString("idmyMatch", "");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("profiles");
        query.whereEqualTo("facebookId", idmyMatch);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> nameList, ParseException e) {
                if (e == null) {
                    if (nameList.size() >= 1) {
                        String pic1 = nameList.get(0).get("pic1").toString();
                        String pic2 = nameList.get(0).get("pic2").toString();
                        String pic3 = nameList.get(0).get("pic3").toString();
                        String pic4 = nameList.get(0).get("pic4").toString();
                        String pic5 = nameList.get(0).get("pic5").toString();
                        if (pic1.compareTo("") != 0)
                            pics.add(pic1);
                        if (pic2.compareTo("") != 0)
                            pics.add(pic2);
                        if (pic3.compareTo("") != 0)
                            pics.add(pic3);
                        if (pic4.compareTo("") != 0)
                            pics.add(pic4);
                        if (pic5.compareTo("") != 0)
                            pics.add(pic5);
                        // aboutme.setText(nameList.get(0).get("aboutme").toString());
                        // aboutme.setSelection(aboutme.getText().length());

                    }
                    i++;
                    initVeiwPager();


                } else {

                }
            }
        });






        return v;
    }


    private void initVeiwPager() {

        viewPager=(ViewPager)v.findViewById(R.id.view_pager);
        viewPager.setPageTransformer(true, new DepthPageTransformer ());
        List<Fragment> listFragments=new ArrayList<Fragment>();
        Fragment f1=new PicProfileFragment();
        f1.setArguments(this.getArguments());

        listFragments.add(f1);
       // listFragments.add(new Fragment1());
        for (int i=0;i<pics.size();i++)
        {
            Fragment f=new PicFragment();
            Bundle bundle=new Bundle();
            bundle.putString("PicPath",pics.get((i)));
            f.setArguments(bundle);
            listFragments.add(f);
        }


        myFragmentPagerAdaper=new MyFragmentPagerAdaper(getChildFragmentManager(),listFragments);
        viewPager.setAdapter(myFragmentPagerAdaper);
        viewPager.setOnPageChangeListener(this);

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
