package com.theboredengineers.easylipo.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.theboredengineers.easylipo.R;
import com.theboredengineers.easylipo.model.BatteryManager;
import com.theboredengineers.easylipo.network.NetworkManager;
import com.theboredengineers.easylipo.network.NetworkSyncListener;
import com.theboredengineers.easylipo.objects.Battery;
import com.theboredengineers.easylipo.ui.fragments.BatteryListFragment;
import io.fabric.sdk.android.Fabric;


public class ActivityBatteryList extends SecuredActivity implements BatteryListFragment.OnBatteryListFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_battery_list);
        Bundle extras = getIntent().getExtras();
        boolean synced = false;
        if (extras != null) {
            synced = extras.getBoolean("synced");
        }


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.battery_list, new BatteryListFragment())
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_battery_list, menu);
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
        if (id == R.id.action_signout) {

           signOut();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBatteryClick(Battery battery) {
        startBatteryDetailsActivity(battery);
    }

    private void startBatteryDetailsActivity(Battery battery) {

        Intent intent = new Intent(this,ActivityBatteryDetails.class);
        intent.putExtra("batteryListPosition",
                BatteryManager.getInstance(this).getBatteryList().indexOf(battery));
        startActivity(intent);
    }
}
