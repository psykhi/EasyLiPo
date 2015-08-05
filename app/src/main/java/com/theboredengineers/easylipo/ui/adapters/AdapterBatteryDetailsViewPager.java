package com.theboredengineers.easylipo.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.theboredengineers.easylipo.objects.Battery;
import com.theboredengineers.easylipo.ui.fragments.BatteryDetailsFragment;

import java.util.ArrayList;

/**
 * Created by Alex on 12/01/2015.
 */
public class AdapterBatteryDetailsViewPager extends FragmentStatePagerAdapter {

    private ArrayList<Battery> batteryList;
    private BatteryDetailsFragment currentFragment;

    public AdapterBatteryDetailsViewPager(FragmentManager fm, ArrayList<Battery> batteryList) {
        super(fm);
        this.batteryList = batteryList;

    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        currentFragment = (BatteryDetailsFragment) object;
    }

    @Override
    public Fragment getItem(int position) {
        return BatteryDetailsFragment.newInstance(position, false);
    }

    @Override
    public int getCount() {
        return batteryList.size();
    }

    public BatteryDetailsFragment getCurrentFragment() {
        return currentFragment;
    }
}
