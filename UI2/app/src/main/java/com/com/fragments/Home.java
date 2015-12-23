package com.com.fragments;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;

import com.com.adapters.MyFragmentPagerAdaper;
import com.inner.fragment.Fragment1;
import com.inner.fragment.Fragment2;
import com.inner.fragment.Fragment3;
import com.inner.fragment.Fragment4;
import com.inner.fragment.Fragment5;
import com.inner.fragment.Fragment6;
import com.peek.matchup.ui2.FakeContant;
import com.peek.matchup.ui2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 06/12/2015.
 */
public class Home extends Fragment implements ViewPager.OnPageChangeListener,TabHost.OnTabChangeListener{

    ViewPager viewPager;
    TabHost tabHost;
    MyFragmentPagerAdaper myFragmentPagerAdaper;
    int i=0;
    View v;
    Fragment2 fragment2;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.tabs_view_pager_layout,container,false);

        i++;
        initVeiwPager();
        initTabHost(savedInstanceState);


        return v;
    }

    private void initTabHost(Bundle savedInstanceState) {
        tabHost=(TabHost)v.findViewById(R.id.tabhost);
        tabHost.setup();
        String [] tabNames={"tab1","tab2","tab3","tab4"};
        int [] icons={R.mipmap.appicon,R.mipmap.connect,R.mipmap.mymatch,R.mipmap.chat};
        for(int i=0;i<tabNames.length;i++)
        {
            TabHost.TabSpec tabSpec;
            tabSpec=tabHost.newTabSpec(tabNames[i]);
            tabSpec.setIndicator("", ContextCompat.getDrawable(getActivity(),icons[i]));
            //tabSpec.setIndicator(tabNames[i],getResources().getDrawable(R.mipmap.home));
            tabSpec.setContent(new FakeContant(getActivity()));
            tabHost.addTab(tabSpec);


        }

        for(int i=0; i<tabNames.length; i++) {
            tabHost.getTabWidget().getChildAt(i).getLayoutParams().height *= 1.7;
            tabHost.getTabWidget().getChildAt(i).getLayoutParams().width= 150;
        }

        tabHost.setOnTabChangedListener(this);


    }

    private void initVeiwPager() {

        viewPager=(ViewPager)v.findViewById(R.id.view_pager);
        fragment2=new Fragment2();
        List<Fragment> listFragments=new ArrayList<Fragment>();
        listFragments.add(new Fragment1());
        listFragments.add(fragment2);
        listFragments.add(new Fragment3());
        listFragments.add(new Fragment4());
        //listFragments.add(new Fragment5());
        //listFragments.add(new Fragment6());
        myFragmentPagerAdaper=new MyFragmentPagerAdaper(getChildFragmentManager(),listFragments);
        viewPager.setAdapter(myFragmentPagerAdaper);
        viewPager.setOnPageChangeListener(this);

    }




    @Override
    public void onTabChanged(String tabId) {
        int SelectedItem=tabHost.getCurrentTab();
        viewPager.setCurrentItem(SelectedItem);
        HorizontalScrollView horizontalScrollView=(HorizontalScrollView)v.findViewById(R.id.h_scroll_view);
        View tabView=tabHost.getCurrentTabView();
        int scrollpos=tabView.getLeft()-(horizontalScrollView.getWidth()-tabView.getWidth())/2;
        horizontalScrollView.smoothScrollTo(scrollpos, 0);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tabHost.setCurrentTab(position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragment2.onActivityResult(requestCode,resultCode,data);
    }
}
