package com.theboredengineers.easylipo.model;

import android.content.Context;
import android.os.AsyncTask;

import com.theboredengineers.easylipo.objects.Battery;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Alexandre on 29/07/2015.
 */
public class ASyncTaskUpdateDB extends AsyncTask<Object, Integer, Integer> {
    @Override
    protected Integer doInBackground(Object... params) {
        Context context = (Context) params[0];
        ArrayList<Battery> updateList = (ArrayList<Battery>) params[1];
        ArrayList<Battery> insertList = (ArrayList<Battery>) params[2];

        Iterator<Battery> insertIterator = insertList.iterator();
        while (insertIterator.hasNext()) {
            BatteryManager.getInstance(context).insertBattery(insertIterator.next());
        }

        Iterator<Battery> updateIterator = updateList.iterator();
        while (updateIterator.hasNext()) {
            BatteryManager.getInstance(context).updateBattery(updateIterator.next());
        }
        return 1;
    }
}
