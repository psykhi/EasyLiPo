package com.theboredengineers.easylipo.ui.activities;

import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.theboredengineers.easylipo.R;

/**
 * Created by Alex on 14/06/2015.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Activity", this.getClass().toString());

    }

    @Override
    protected void onResume() {
        super.onResume();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getTitle());
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getTitle());
        }
    }


}
