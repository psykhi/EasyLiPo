package com.theboredengineers.easylipo.ui.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.theboredengineers.easylipo.R;
import com.theboredengineers.easylipo.model.BatteryManager;
import com.theboredengineers.easylipo.objects.Battery;
import com.theboredengineers.easylipo.objects.NfcTag;
import com.theboredengineers.easylipo.ui.adapters.AdapterBatteryDetailsViewPager;
import com.theboredengineers.easylipo.ui.fragments.BatteryDetailsFragment;

import java.util.ArrayList;

public class ActivityBatteryDetails extends NfcActivity implements ViewPager.OnPageChangeListener,
        BatteryDetailsFragment.OnBatteryDetailsInteractionLister{


    private Battery batt;
    private int position;
    private BatteryManager batteryManager;
    private ArrayList<Battery> batteryList;
    private ViewPager viewPager;
    private AdapterBatteryDetailsViewPager adapterBatteryDetailsViewPager;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_details);
        Intent intent = getIntent();

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            //NfcTag.BuildFromTag((Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG));
            String serverID = NfcTag.getServerIDFromIntent(intent);
            Battery batt = BatteryManager.getInstance(this).getBatteryByServerId(serverID);
            if(batt != null)
            {
                position = BatteryManager.getInstance(this).getBatteryList().indexOf(batt);
            }
            else
            {
                // This battery is not ours => TODO
                Toast.makeText(this,"This battery is not yours !! TODO",Toast.LENGTH_SHORT).show();
            }


        }
        else {
            position = intent.getIntExtra("batteryListPosition", 0);
        }

        batteryManager =  BatteryManager.getInstance(this);
        batteryList = batteryManager.getBatteryList();



        viewPager = (ViewPager) findViewById(R.id.viewPager_activity_details);
        adapterBatteryDetailsViewPager =
                new AdapterBatteryDetailsViewPager(getSupportFragmentManager(),batteryList);
        viewPager.setAdapter(adapterBatteryDetailsViewPager);
        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(this);
        setTitle(batteryManager.getBatteryList().get(position).getName());
        //setSupportActionBar((Toolbar) findViewById(R.id.details_toolbar));
        //getSupportActionBar().setTitle(batteryManager.getBatteryList().get(position).getName());

    }


    @Override
    protected void onResume() {
        super.onResume();
        setTitle(batteryManager.getBatteryList().get(position).getName());
        getSupportActionBar().setTitle(this.getTitle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_battery_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            onEditButtonClicked(position);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.position = position;
        setTitle(batteryManager.getBatteryList().get(position).getName());

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onEditButtonClicked(int position) {

        Intent intent = new Intent(this,ActivityBatteryEdit.class);
        intent.putExtra("batteryListPosition",position);
        startActivity(intent);
    }
}
