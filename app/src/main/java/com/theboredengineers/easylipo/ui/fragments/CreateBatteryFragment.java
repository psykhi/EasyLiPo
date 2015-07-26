package com.theboredengineers.easylipo.ui.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.theboredengineers.easylipo.R;
import com.theboredengineers.easylipo.model.BatteryManager;
import com.theboredengineers.easylipo.network.NetworkManager;
import com.theboredengineers.easylipo.objects.Battery;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreateBatteryFragment extends Fragment {

    private Button okButton;
    public CreateBatteryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_activity_create_battery, container, false);
        okButton = (Button) v.findViewById(R.id.buttonCreateBatteryOK);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Battery b = new Battery();
                b.setBrand("hk");
                b.setModel("lol");
                b.setName("KOUKOU");
                b.setNbS(3);
                if(BatteryManager.getInstance(getActivity()).insertOrUpdateBattery(b) != -1)
                {
                    NetworkManager.getInstance().addNewBattery(b);
                }
                else
                {
                    Toast.makeText(getActivity(),"Failed to insert the battery in DB",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }
}
