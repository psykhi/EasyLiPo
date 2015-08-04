package com.theboredengineers.easylipo.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.theboredengineers.easylipo.objects.Battery;
import com.theboredengineers.easylipo.ui.fragments.FragmentNewBattery;

/**
 * Created by Alex on 14/01/2015.
 */
public class AdapterNewBatteryViewPager extends FragmentPagerAdapter {


    private Battery battery;

    public AdapterNewBatteryViewPager(FragmentManager fm, Battery battery) {
        super(fm);
        this.battery = battery;
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentNewBattery.newInstance(position, battery);
    }

    @Override
    public int getCount() {
        return 3;
    }
}

