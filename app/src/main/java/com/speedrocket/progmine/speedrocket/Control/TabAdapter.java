package com.speedrocket.progmine.speedrocket.Control;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.speedrocket.progmine.speedrocket.View.Fragment.AboutTab;



public class TabAdapter extends FragmentStatePagerAdapter
{

    int numOfTabs ;

    public TabAdapter(FragmentManager fm , int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs ;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:

                AboutTab tab1 = new AboutTab();
                return tab1;

            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return numOfTabs;
    }
}
