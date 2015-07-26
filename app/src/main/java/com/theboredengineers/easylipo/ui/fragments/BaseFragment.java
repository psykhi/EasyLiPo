package com.theboredengineers.easylipo.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.theboredengineers.easylipo.interfaces.BatteryListChangedListener;
import com.theboredengineers.easylipo.model.BatteryManager;

/**
 * Created by Alex on 17/06/2015.
 */
public class BaseFragment extends Fragment implements BatteryListChangedListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        BatteryManager.getInstance(getActivity()).addListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BatteryManager.getInstance(getActivity()).removeListener(this);
    }

    @Override
    public void onBatteryListChanged() {

    }
}
