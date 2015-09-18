package com.theboredengineers.easylipo.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.theboredengineers.easylipo.R;
import com.theboredengineers.easylipo.model.BatteryManager;
import com.theboredengineers.easylipo.objects.Battery;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class BatteryDetailsFragment extends BaseFragment {

    private TextView cyclesView;
    private Battery battery;
    private FloatingActionButton buttonAddCycle;
    private EditText brand;
    private OnBatteryDetailsInteractionLister listener;
    private EditText model;
    private EditText capacity;
    private EditText dateText, charge, discharge, cells;
    private Date date;
    private Switch charged;
    private boolean inEditMode;


    public BatteryDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_activity_battery_details, container, false);
        int position = getArguments().getInt("batteryListPosition");
        battery =BatteryManager.getInstance(getActivity())
                .getBatteryList().get(position);
        cyclesView = (TextView) v.findViewById(R.id.textViewCycleCount);
        brand = (EditText) v.findViewById(R.id.details_brand);
        model = (EditText) v.findViewById(R.id.details_model);
        capacity = (EditText) v.findViewById(R.id.details_capacity);
        discharge = (EditText) v.findViewById(R.id.details_discharge);
        charge = (EditText) v.findViewById(R.id.details_charge);
        dateText = (EditText) v.findViewById(R.id.details_date);
        cells = (EditText) v.findViewById(R.id.details_cells);
        charged = (Switch) v.findViewById(R.id.details_charged);
        date = battery.getPurchaseDate();

        charged.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b != battery.isCharged()) {
                    changeChargedBattery(b);
                }
            }
        });


        buttonAddCycle = (FloatingActionButton) v.findViewById(R.id.buttonBatteryDetailsAddCycle);
        buttonAddCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInEditMode()) {
                    listener.onEditDone();
                    setInEditMode(false);
                } else {
                    BatteryManager.getInstance(getActivity()).addCycle(battery);
                    setCycleCount(battery.getNbOfCycles());
                    if (battery.isCharged()) {
                        battery.setCharged(false);
                        charged.setChecked(false);
                        BatteryManager.getInstance(getActivity()).updateBattery(battery);
                    }
                }
            }
        });


        return v;
    }

    private void changeChargedBattery(boolean charged) {
        battery.setCharged(charged);
        BatteryManager.getInstance(getActivity()).updateBattery(battery);
    }
    @Override
    public void onResume() {
        super.onResume();
        fillUI();
    }

    private void fillUI() {
        Log.d("Details", battery.toString());

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy", Locale.US);
        if (battery.getPurchaseDate() != null)
            dateText.setText(format.format(battery.getPurchaseDate()));
        brand.setText(battery.getBrand());
        model.setText(battery.getModel());
        capacity.setText(String.valueOf(battery.getCapacity()));
        cells.setText(String.valueOf(battery.getNbS()));
        discharge.setText(String.valueOf(battery.getDischargeRate()));
        charge.setText(String.valueOf(battery.getChargeRate()));
        charged.setChecked(battery.isCharged());
        this.setCycleCount(battery.getNbOfCycles());
    }


    private void setCycleCount(int cycles) {
        cyclesView.setText(cycles + " " + this.getString(R.string.cycles));
    }


    public static BatteryDetailsFragment newInstance(int position, boolean edit)
    {
        BatteryDetailsFragment ret = new BatteryDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("batteryListPosition", position);
        args.putBoolean("edit", edit);
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

    public boolean isInEditMode() {
        return inEditMode;
    }

    public void setInEditMode(boolean inEditMode) {
        this.inEditMode = inEditMode;
        if (isInEditMode()) {
            //buttonAddCycle.setImageDrawable(getActivity().getDrawable(R.drawable.ic_edit));
            buttonAddCycle.setIcon(R.mipmap.ic_ok);
            brand.setEnabled(true);
            model.setEnabled(true);
            cells.setEnabled(true);
            capacity.setEnabled(true);
            dateText.setEnabled(true);
            discharge.setEnabled(true);
            charge.setEnabled(true);
        } else {
            //buttonAddCycle.setImageDrawable(getActivity().getDrawable(R.drawable.));
            buttonAddCycle.setIcon(R.mipmap.ic_plus_one);
            brand.setEnabled(false);
            model.setEnabled(false);
            cells.setEnabled(false);
            capacity.setEnabled(false);
            dateText.setEnabled(false);
            discharge.setEnabled(false);
            charge.setEnabled(false);
            updateBatteryInfo();
        }
    }

    private void updateBatteryInfo() {
        battery.setChargeRate(Integer.parseInt(charge.getText().toString()));
        battery.setDischargeRate(Integer.parseInt(discharge.getText().toString()));
        battery.setModel(model.getText().toString());
        battery.setBrand(brand.getText().toString());
        battery.setPurchaseDate(date);
        battery.setCapacity(Integer.parseInt(capacity.getText().toString()));
        BatteryManager.getInstance(getActivity()).updateBattery(battery);
    }

    public interface OnBatteryDetailsInteractionLister {
        void onEditButtonClicked(int position);

        void onEditDone();
    }

    @Override
    public void onBatteryListChanged() {
        super.onBatteryListChanged();

    }
}
