package com.theboredengineers.easylipo.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.theboredengineers.easylipo.R;
import com.theboredengineers.easylipo.model.BatteryManager;
import com.theboredengineers.easylipo.network.NetworkManager;
import com.theboredengineers.easylipo.objects.Battery;

/**
 * A placeholder fragment containing a simple view.
 */
public class BatteryDetailsFragment extends BaseFragment {

    private Battery battery;
    private Button buttonAddCycle;
    private Button buttonEdit;
    private int position;
    private OnBatteryDetailsInteractionLister listener;

    public BatteryDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_activity_battery_details, container, false);
        position = getArguments().getInt("batteryListPosition");
        battery =BatteryManager.getInstance(getActivity())
                .getBatteryList().get(position);
        buttonAddCycle = (Button) v.findViewById(R.id.battery_details_add_cycle);
        buttonAddCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BatteryManager.getInstance(getActivity()).addCycle(battery);
                if (!battery.isLocal()) {
                    NetworkManager.getInstance().addCycle(battery);
                }
            }
        });
        buttonEdit = (Button) v.findViewById(R.id.buttonDetailsEdit);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEditButtonClicked(position);
            }
        });
        return v;
    }
    public static BatteryDetailsFragment newInstance(int position)
    {
        BatteryDetailsFragment ret = new BatteryDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("batteryListPosition", position);
        ret.setArguments(args);
        return ret;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (BatteryDetailsFragment.OnBatteryDetailsInteractionLister) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    public interface OnBatteryDetailsInteractionLister {
        public void onEditButtonClicked(int position);
    }
}
