package com.merit.myapplication.moduls;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[];
    ArrayList<Fragment> fragments;

    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], ArrayList<Fragment> fragments) {
        super(fm);
        this.Titles = mTitles;
        this.fragments = fragments;
    }

    // This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return Titles.length;
    }
}