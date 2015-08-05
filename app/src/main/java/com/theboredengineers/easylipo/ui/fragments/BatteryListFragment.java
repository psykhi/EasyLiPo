package com.theboredengineers.easylipo.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.theboredengineers.easylipo.R;
import com.theboredengineers.easylipo.model.BatteryManager;
import com.theboredengineers.easylipo.network.NetworkManager;
import com.theboredengineers.easylipo.network.listeners.NetworkSyncListener;
import com.theboredengineers.easylipo.objects.Battery;
import com.theboredengineers.easylipo.ui.DividerItemDecoration;
import com.theboredengineers.easylipo.ui.activities.ActivityCreateBattery;
import com.theboredengineers.easylipo.ui.adapters.AdapterBatteryList;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;


/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnBatteryListFragmentInteractionListener}
 * interface.
 */
public class BatteryListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{


    private OnBatteryListFragmentInteractionListener mListener;
    private AdapterBatteryList adapterBatteryList;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout layout;
    FloatingActionButton addButton;

    public static BatteryListFragment newInstance(boolean sync) {
        BatteryListFragment fragment = new BatteryListFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BatteryListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        temp_create_db(batteryManager);




    }


    /**
     * @brief Updates the recyclerView with the battery DB. This method must be called after the
     * onCreateView call.
     */
    public void updateListWithDatabase() {
        ArrayList<Battery> batteryList;
        if (null == BatteryManager.getInstance(getActivity()))
            return;
        //TODO select sorting method by reading SharedPreferences
        batteryList = BatteryManager.getInstance(getActivity()).getBatteryList();
        if (batteryList != null) {
            if(adapterBatteryList == null) {
                adapterBatteryList = new AdapterBatteryList((OnBatteryListFragmentInteractionListener) getActivity(), 0, batteryList);
                recyclerView.setAdapter(adapterBatteryList);
                recyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(adapterBatteryList));
                RecyclerView.ItemDecoration itemDecoration =
                        new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
                recyclerView.addItemDecoration(itemDecoration);
            }
//TODO handle empty lists
            adapterBatteryList.notifyDataSetChanged();

        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_battery_list, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerviewBatteries);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        layout = (SwipeRefreshLayout) v.findViewById(R.id.battery_swipe);
        layout.setOnRefreshListener(this);
        addButton = (FloatingActionButton)v.findViewById(R.id.buttonBatteryListAddBattery);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCreateBatteryActivity();
            }
        });


        return v;
    }

    private void goToCreateBatteryActivity() {
        Intent intent = new Intent(getActivity(), ActivityCreateBattery.class);
        getActivity().startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateListWithDatabase();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnBatteryListFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onBatteryListChanged() {
        adapterBatteryList.setBatteryList(BatteryManager.getInstance(getActivity()).getBatteryList());
    }

    @Override
    public void onRefresh() {
        NetworkManager.getInstance().networkSync(getActivity(),new NetworkSyncListener() {
            @Override
            public void onNetworkSyncEnded(Boolean success, String errorMessageFromJSON) {
                layout.setRefreshing(false);
                if(!success)
                {
                    Toast.makeText(getActivity(), errorMessageFromJSON, Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnBatteryListFragmentInteractionListener {
        void onBatteryClick(Battery batt);
    }

}
