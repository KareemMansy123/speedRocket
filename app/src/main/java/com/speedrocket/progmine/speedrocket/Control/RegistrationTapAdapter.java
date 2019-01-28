package com.speedrocket.progmine.speedrocket.Control;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.speedrocket.progmine.speedrocket.View.Fragment.RegistrationFirstTap;
import com.speedrocket.progmine.speedrocket.View.Fragment.RegistrationSecTap;
import com.speedrocket.progmine.speedrocket.View.Fragment.RegistrationVirevifyTap;

public class RegistrationTapAdapter extends FragmentStatePagerAdapter {

    int numOfTabs ;
    public RegistrationTapAdapter(FragmentManager fm , int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs ;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                RegistrationFirstTap tab1 = new RegistrationFirstTap();
                return tab1;
            case 1:
                RegistrationVirevifyTap tab2 = new RegistrationVirevifyTap();
                return tab2;
            case 2:
                RegistrationSecTap tab3 = new RegistrationSecTap();
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
