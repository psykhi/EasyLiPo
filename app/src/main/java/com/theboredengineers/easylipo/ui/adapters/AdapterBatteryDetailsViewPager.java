package com.theboredengineers.easylipo.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.theboredengineers.easylipo.objects.Battery;
import com.theboredengineers.easylipo.ui.fragments.BatteryDetailsFragment;

import java.util.ArrayList;

/**
 * Created by Alex on 12/01/2015.
 */
public class AdapterBatteryDetailsViewPager extends FragmentStatePagerAdapter {

    private ArrayList<Battery> batteryList;

    public AdapterBatteryDetailsViewPager(FragmentManager fm, ArrayList<Battery> batteryList) {
        super(fm);
        this.batteryList = batteryList;

    }

    @Override
    public Fragment getItem(int position) {
        BatteryDetailsFragment fragment = BatteryDetailsFragment.newInstance(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return batteryList.size();
    }
}
