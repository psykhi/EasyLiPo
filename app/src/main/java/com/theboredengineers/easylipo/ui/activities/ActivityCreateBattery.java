package com.theboredengineers.easylipo.ui.activities;

import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.theboredengineers.easylipo.R;
import com.theboredengineers.easylipo.model.BatteryManager;
import com.theboredengineers.easylipo.network.NetworkManager;
import com.theboredengineers.easylipo.objects.Battery;
import com.theboredengineers.easylipo.ui.adapters.AdapterNewBatteryViewPager;
import com.theboredengineers.easylipo.ui.fragments.FragmentNewBattery;
import com.theboredengineers.easylipo.ui.fragments.FragmentNewBattery.BatterySupplier;

public class ActivityCreateBattery extends NfcActivity implements FragmentNewBattery.OnNewBatteryFragmentNextButtonClicked,
        BatterySupplier {

    private ViewPager viewPager;
    private Battery battery;
    private AdapterNewBatteryViewPager adapterNewBatteryViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        battery = new Battery();
        setContentView(R.layout.activity_create_battery);
        viewPager = (ViewPager) findViewById(R.id.viewPager_new_battery);
        adapterNewBatteryViewPager =
                new AdapterNewBatteryViewPager(getSupportFragmentManager(), battery);
        viewPager.setAdapter(adapterNewBatteryViewPager);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(R.string.new_battery);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_create_battery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNext(int step) {
        if (step < FragmentNewBattery.BATTERY_DATE)
            viewPager.setCurrentItem(step + 1);
        else
            waitForNFC();
    }

    @Override
    protected void onNfcTagScanned(Tag tag, Ndef ndef) {
        super.onNfcTagScanned(tag, ndef);
    }

    @Override
    public void onDone() {
        BatteryManager.getInstance(this).insertBattery(battery);
        NetworkManager.getInstance().addNewBattery(this, battery);
        finish();
    }

    @Override
    public Battery getBattery() {
        return battery;
    }
}
