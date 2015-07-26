package com.theboredengineers.easylipo.ui.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.theboredengineers.easylipo.R;
import com.theboredengineers.easylipo.model.BatteryManager;
import com.theboredengineers.easylipo.objects.Battery;
import com.theboredengineers.easylipo.objects.NfcPendingIntentHelper;
import com.theboredengineers.easylipo.objects.NfcTag;
import com.theboredengineers.easylipo.ui.fragments.BatteryEditFragment;
import com.theboredengineers.easylipo.ui.fragments.BatteryListFragment;

public class ActivityBatteryEdit extends NfcActivity implements BatteryEditFragment.OnBatteryEditFragmentInteractionListener{


    private Battery battery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_edit);
        int position = getIntent().getIntExtra("batteryListPosition",0);

        if (savedInstanceState == null) {
            BatteryEditFragment frag = BatteryEditFragment.newInstance(position);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.battery_edit, frag)
                    .commit();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_battery_edit, menu);
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
    public void onChangeNFCTag(Battery batt) {

        this.battery = batt;
        waitForNFC();


    }


    @Override
    protected void onNfcTagScanned(Tag tag, Ndef ndef) {
        NfcTag.formatNdef(ndef, battery.getServer_id(), true);

        battery.setTagID(NfcTag.BuildFromTag(tag));
        BatteryManager.getInstance(this).insertOrUpdateBattery(battery);
    }

    @Override
    public void onEdit(Battery batt) {
        finish();
    }
}
