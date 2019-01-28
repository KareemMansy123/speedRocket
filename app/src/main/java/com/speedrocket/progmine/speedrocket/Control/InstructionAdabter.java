package com.speedrocket.progmine.speedrocket.Control;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.speedrocket.progmine.speedrocket.View.Fragment.InstructionsTab;

/**
 * Created by Ibrahim on 9/6/2018.
 */

public class InstructionAdabter extends FragmentStatePagerAdapter {

    int numOfTabs ;
    public InstructionAdabter(FragmentManager fm , int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs ;
    }


    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();

        bundle.putInt("position" , position);


        InstructionsTab tab1 = new InstructionsTab();
        tab1.setArguments(bundle);
        return tab1;

    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

}
