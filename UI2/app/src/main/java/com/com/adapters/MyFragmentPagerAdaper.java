package com.com.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by User on 05/12/2015.
 */
public class MyFragmentPagerAdaper extends FragmentPagerAdapter {

    List<Fragment> listFragment;
    public MyFragmentPagerAdaper(FragmentManager fm, List<Fragment> listFragment) {
        super(fm);
        this.listFragment=listFragment;
    }


    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }
}
