package com.theboredengineers.easylipo.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theboredengineers.easylipo.R;
import com.theboredengineers.easylipo.objects.Battery;
import com.theboredengineers.easylipo.ui.fragments.BatteryListFragment;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;

/**
 * Created by Alex on 14/06/2015.
 */
public class AdapterBatteryList extends RecyclerView.Adapter<AdapterBatteryList.BatteryViewHolder>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    private ArrayList<Battery> batteryList;
    private BatteryListFragment.OnBatteryListFragmentInteractionListener activity;
    public AdapterBatteryList(BatteryListFragment.OnBatteryListFragmentInteractionListener activity, int i, ArrayList<Battery> batteryList) {
        this.batteryList = batteryList;
        this.activity = activity;
    }

    public void setBatteryList(ArrayList<Battery> batteries)
    {
        batteryList = batteries;
        notifyDataSetChanged();
    }

    public class BatteryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public View mTextView;
        private Battery batt;
        private BatteryListFragment.OnBatteryListFragmentInteractionListener listener;

        public BatteryViewHolder(View v, BatteryListFragment.OnBatteryListFragmentInteractionListener listener) {
            super(v);
            v.setOnClickListener(this);
            mTextView = v;
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            if(batt != null) {
                Log.d("batt", "batt " + batt.getName() + " was clicked");
                listener.onBatteryClick(batt);
            }

            else
                Log.e("lol","cliockeeeeee");
        }

        public void setBattery(Battery b)
        {
            this.batt = b;
        }
    }

    @Override
    public BatteryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_battery_list, parent, false);


        return new BatteryViewHolder(convertView,this.activity){

        };
    }

    @Override
    public void onBindViewHolder(BatteryViewHolder holder, int position) {

        View convertView = holder.itemView;
        Battery currentBattery = batteryList.get(position);
        holder.setBattery(currentBattery);


        TextView textViewName = (TextView) convertView.findViewById(R.id.textView_details_battery_name);
        TextView textViewCycles = (TextView) convertView.findViewById(R.id.textView_details_cycles);
        TextView textViewDescription = (TextView) convertView.findViewById(R.id.textView_details_brand_model);

        textViewName.setText(currentBattery.getName());
        textViewCycles.setText(currentBattery.getNbOfCycles()+"\ncycles");
        textViewDescription.setText(currentBattery.getBrand()+" "+currentBattery.getModel()+"\n"
                +currentBattery.getCapacity()+"mAh "+currentBattery.getFormattedDischargeRate());
    }

    @Override
    public long getHeaderId(int i) {
        return batteryList.get(i).getNbS();
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_battery_list_header,viewGroup,false);
        return new RecyclerView.ViewHolder(view)
        {

        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        TextView text = (TextView) viewHolder.itemView
                .findViewById(R.id.textView_item_battery_list_header);
        text.setText(batteryList.get(i).getNbS()+"S");

    }

    @Override
    public int getItemCount() {
        return batteryList.size();
    }
}
