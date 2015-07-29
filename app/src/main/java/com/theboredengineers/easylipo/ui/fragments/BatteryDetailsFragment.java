package com.theboredengineers.easylipo.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.theboredengineers.easylipo.R;
import com.theboredengineers.easylipo.model.BatteryManager;
import com.theboredengineers.easylipo.network.NetworkManager;
import com.theboredengineers.easylipo.objects.Battery;

/**
 * A placeholder fragment containing a simple view.
 */
public class BatteryDetailsFragment extends BaseFragment {

    private TextView cyclesView;
    private Battery battery;
    private FloatingActionButton buttonAddCycle;
    private int position;
    private EditText brand;
    private OnBatteryDetailsInteractionLister listener;
    private EditText model;
    private EditText capacity;

    public BatteryDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_activity_battery_details, container, false);
        position = getArguments().getInt("batteryListPosition");
        battery =BatteryManager.getInstance(getActivity())
                .getBatteryList().get(position);
        cyclesView = (TextView) v.findViewById(R.id.textViewCycleCount);
        brand = (EditText) v.findViewById(R.id.editTextDetailsBrand);
        model = (EditText) v.findViewById(R.id.editTextDetailsModel);
        capacity = (EditText) v.findViewById(R.id.editTextDetailsCapacity);


        buttonAddCycle = (FloatingActionButton) v.findViewById(R.id.buttonBatteryDetailsAddCycle);
        buttonAddCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //cyclesView.animate(battery.getNbOfCycles(),battery.getNbOfCycles() + 1);
                BatteryManager.getInstance(getActivity()).addCycle(battery);
                setCycleCount(battery.getNbOfCycles());
                if (!battery.isLocal()) {
                    NetworkManager.getInstance().addCycle(getActivity(), battery);
                }
            }
        });


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        fillUI();
    }

    private void fillUI() {
        Log.d("Details", battery.toString());
        brand.setText(battery.getBrand());
        model.setText(battery.getModel());
        capacity.setText(battery.getCapacity() + " mAh");
        this.setCycleCount(battery.getNbOfCycles());
    }


    private void setCycleCount(int cycles) {
        cyclesView.setText(cycles + " " + this.getString(R.string.cycles));
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
                    + " must implement OnBatteryDetailsInteractionLister");
        }
    }
    public interface OnBatteryDetailsInteractionLister {
        public void onEditButtonClicked(int position);
    }

    @Override
    public void onBatteryListChanged() {
        super.onBatteryListChanged();

    }
}
