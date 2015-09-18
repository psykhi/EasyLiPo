package com.theboredengineers.easylipo.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.theboredengineers.easylipo.R;

/**
 * Created by Alex on 14/06/2015.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        bindViews();
        setListeners();
        //Log.d("Activity started : ", this.getClass().toString());
    }

    protected void bindViews() {

    }

    protected void setView() {

    }

    protected void setListeners() {


    }

    @Override
    protected void onResume() {
        super.onResume();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null) {
            toolbar.setTitle(getTitle());
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            toolbar.setTitle(getTitle());
            setSupportActionBar(toolbar);
        }
    }


}
