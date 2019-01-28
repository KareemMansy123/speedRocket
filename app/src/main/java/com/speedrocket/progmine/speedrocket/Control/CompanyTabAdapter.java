package com.speedrocket.progmine.speedrocket.Control;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.speedrocket.progmine.speedrocket.View.Fragment.CompanyAboutTab;
import com.speedrocket.progmine.speedrocket.View.Fragment.CompanyLatestOfferTab;
import com.speedrocket.progmine.speedrocket.View.Fragment.CompanyProductTab;

/**
 * Created by Ibrahim on 3/21/2018.
 */

public class CompanyTabAdapter extends FragmentStatePagerAdapter
{

    int numOfTabs ;
    public CompanyTabAdapter(FragmentManager fm , int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs ;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                CompanyAboutTab tab1 = new CompanyAboutTab();
                return tab1;
            case 1:
                CompanyLatestOfferTab tab2 = new CompanyLatestOfferTab();
                return tab2;
            case 2:
                CompanyProductTab tab3 = new CompanyProductTab();
                return tab3;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
