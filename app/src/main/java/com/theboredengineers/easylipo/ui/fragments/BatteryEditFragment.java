package com.theboredengineers.easylipo.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.theboredengineers.easylipo.R;
import com.theboredengineers.easylipo.model.BatteryManager;
import com.theboredengineers.easylipo.network.NetworkManager;
import com.theboredengineers.easylipo.objects.Battery;

/**
 * A placeholder fragment containing a simple view.
 */
public class BatteryEditFragment extends BaseFragment {

    private EditText name,brand,model,capacity,cells;
    private EditText dateText;
    private int position;
    private Battery battery;
    private Button buttonChangeNFC;
    private FloatingActionButton buttonOK;
    private OnBatteryEditFragmentInteractionListener listener;

    public BatteryEditFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_battery_edit, container, false);
        name = (EditText) v.findViewById(R.id.editTextBatteryEditName);
        brand = (EditText) v.findViewById(R.id.editTextBatteryEditBrand);
        model = (EditText) v.findViewById(R.id.editTextBatteryEditModel);
        capacity = (EditText) v.findViewById(R.id.editTextBatteryEditCapacity);
        cells = (EditText) v.findViewById(R.id.editTextBatteryEditCells);
        dateText = (EditText) v.findViewById(R.id.editTextBatteryEditBuyDate);
        buttonChangeNFC = (Button) v.findViewById(R.id.buttonEditBatteryAddNFC);
        buttonOK = (FloatingActionButton) v.findViewById(R.id.buttonEditBatteryOK);

        position = getArguments().getInt("batteryListPosition");
        battery = BatteryManager.getInstance(getActivity()).getBatteryList().get(position);

        name.setText(battery.getName());
        brand.setText(battery.getBrand());
        model.setText(battery.getModel());
        cells.setText(Integer.toString(battery.getNbS()));
        capacity.setText(Integer.toString(battery.getCapacity()));
        dateText.setText(battery.getPurchaseDate().toString());

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
                buttonChangeNFC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onChangeNFCTag(battery);
                    }
                });
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBattery(battery);
            }
        });
        return v;
    }

    private void editBattery(Battery battery) {
        battery.setName(name.getText().toString());
        battery.setModel(model.getText().toString());
        battery.setBrand(brand.getText().toString());
        battery.setCapacity(Integer.parseInt(capacity.getText().toString()));
        battery.setNbS(Integer.parseInt(cells.getText().toString()));
        BatteryManager.getInstance(getActivity()).updateBattery(battery);
        NetworkManager.getInstance().updateBattery(getActivity(), battery);
        listener.onEdit(battery);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnBatteryEditFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    public static BatteryEditFragment newInstance(int position) {
        BatteryEditFragment ret = new BatteryEditFragment();
        Bundle args = new Bundle();
        args.putInt("batteryListPosition", position);
        ret.setArguments(args);
        return ret;
    }

    public interface OnBatteryEditFragmentInteractionListener {
        public void onChangeNFCTag(Battery batt);
        public void onEdit(Battery batt);
    }
}
