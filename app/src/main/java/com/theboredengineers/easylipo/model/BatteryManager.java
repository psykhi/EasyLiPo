package com.theboredengineers.easylipo.model;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.theboredengineers.easylipo.interfaces.BatteryListChangedListener;
import com.theboredengineers.easylipo.objects.Battery;
import com.theboredengineers.easylipo.objects.NfcTag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by benoit.hocquet on 06/01/2015.
 * Wrapper for db calls to manage batteries.
 */
public class BatteryManager {

    private BatterySQLite sqlBat;
    private static ArrayList<Battery> list;
    private static BatteryManager INSTANCE;
    private Context context = null;
    private ArrayList<BatteryListChangedListener> listeners;

    /**
     * Private constructor. We create the battery list
     *
     * @param context
     */
    private BatteryManager(Context context) {
        list = new ArrayList<Battery>();
        sqlBat = new BatterySQLite(context);
        listeners = new ArrayList<BatteryListChangedListener>();
        this.context = context;
        refreshList();
    }

    public void addListener(BatteryListChangedListener listener)
    {
        listeners.add(listener);
    }

    public void removeListener(BatteryListChangedListener listener)
    {
        listeners.remove(listener);
    }

    private void notifyListeners()
    {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).onBatteryListChanged();
                }
            }
        });

    }
    /**
     * Refreshes the battery list with the current batteries
     */
    private void refreshList() {
        //TODO sharedpreferences
        sqlBat.getBatteriesByCapacity(list);
        Iterator<Battery> iterator = list.iterator();
        while (iterator.hasNext()) {
            Battery batt = iterator.next();
            Log.d("List", batt.toString());
        }

        this.notifyListeners();
    }

    /**
     * get an instance of the singleton
     *
     * @param context Context
     * @return instance
     */
    public static BatteryManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new BatteryManager(context);
        }
        return INSTANCE;
    }

    /**
     * Add a battery in the system, or modify an existing battery.
     * An battery's id different from -1 is considered as existing in the system.
     *
     * @param battery battery to be inserted (if succeeded its id will be changed)
     * @return the id of the inserted battery, -1 if failed.
     */
    public int insertBattery(Battery battery) {
        Log.d("Battery", "inserting " + battery.toString());
        list.add(battery);
        int ret = sqlBat.insertBattery(battery);
        sortBatteries();
        return ret;
    }

    public int updateBattery(Battery battery) {
        Log.d("Battery", "updating " + battery.toString());
        int ret = sqlBat.updateBattery(battery);
        sortBatteries();
        return ret;
    }

    public Boolean addCycle(Battery battery)
    {
        battery.setNbOfCycles(battery.getNbOfCycles() + 1);
        if (updateBattery(battery) != -1)
            return true;
        else
            return false;
    }
    /**
     * Get a battery from system
     *
     * @param id the id of the battery to be retrieved
     * @return the found battery, or null otherwise.
     */
    public Battery getBatteryByLocalId(int id) {
        return sqlBat.getBatteryById(id);
    }

    public Battery getBatteryByServerId(String id) {
        Battery b = new Battery();
        b.setServer_id(id);
        if(list.contains(b))
            return list.get(list.indexOf(b));
        else return null;
        //return sqlBat.getBatteryByServerId(id);
    }

    /**
     * Remove all the batteries from system
     */
    public void removeAllBatteries() {
        sqlBat.removeAllBatteries();
        refreshList();
        notifyListeners();
    }


    /**
     * Get all batteries that are inserted in the system
     *
     * @return the list of batteries
     */
    public ArrayList<Battery> getAllBatteries() {
        //return sqlBat.getAllBatteries();
        //TODO
        return null;
    }

    public ArrayList<Battery> getBatteryList() {

        return this.list;
    }


    /**
     * Get a battery in the system from its tag
     *
     * @param tag the tag of the battery
     * @return the found battery, null otherwise.
     */
    public Battery getBatteryByTag(NfcTag tag) {
        return sqlBat.getBatteryByTag(tag);
    }

    /**
     * Get Batteries sorted by capacity for any given S, P,
     *
     * @return the found batteries
     */
    private ArrayList<Battery> getBatteriesByCapacity() {
        //return sqlBat.getBatteriesByCapacity();
        //TODO
        return null;
    }

    /**
     * Get Batteries sorted by name
     *
     * @return the batteries
     */
    public ArrayList<Battery> getBatteriesByName() {
        //  return sqlBat.getBatteriesByName();
        //TODO
        return null;
    }


    /**
     * Remove a battery from the system.
     * @param bat the battery to remove (the id only is checked). Id will be set to -1 if removed.
     * @return true if a battery has been removed, else otherwise.
     */
    public boolean removeBattery(Battery bat) {
        list.remove(bat);
        boolean returnCode =  removeBattery(bat.getId());
        if(returnCode)
            bat.setID(-1); // The battery is not in the system anymore
        notifyListeners();
        return returnCode;
    }

    /**
     *
     * @param id
     * @return
     */
    private boolean removeBattery(int id) {
        boolean returnCode =  sqlBat.removeBattery(id);

        return returnCode;
    }

    public boolean removeBattery(NfcTag nfcTag) {
        boolean returnCode =  sqlBat.removeBattery(nfcTag);
        if(returnCode)
            refreshList();
        return returnCode;
    }

    /**
     * Used only in tests...
     */
    public void closeDB() {
        sqlBat.close();
        INSTANCE = null;
    }

    private ArrayList<Battery> getListFromJSON(Object json) {
        ArrayList<Battery> newList = new ArrayList<Battery>();


        if (json instanceof JSONObject) {
            return null;
        } else if (json instanceof JSONArray) {
            JSONArray array = (JSONArray) json;

            for (int i = 0; i < array.length(); i++) {
                try {
                    newList.add(getBatteryFromJSON(array.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return newList;
    }

    /**
     * @param json
     * @brief Merges the server's list with ours
     */
    public void setBatteries(Object json) {
        ArrayList<Battery> serverList = getListFromJSON(json);
        ArrayList<Battery> toRe = new ArrayList<Battery>();
        if (serverList != null) {

            // Now we have the new list. We'll go through the "old" local list and see the changes
            //with the new list
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                Battery old = (Battery) iterator.next();

                // First we remove the batteries missing from the server
                if (!serverList.contains(old)) {
                    // The local battery has been deleted :( RIP
                    Log.d("DELETE", "REP " + old);
                    removeBattery(old.getId());
                    iterator.remove();

                    //TODO should we start over here since the order has changed ??
                }
            }
            // Then we can safely replace our local batteries with the ones from the server
            Iterator<Battery> updateIterator = serverList.iterator();
            ArrayList<Battery> updateList = new ArrayList<Battery>();
            ArrayList<Battery> insertList = new ArrayList<Battery>();
            while (updateIterator.hasNext()) {
                Battery serverBattery = (Battery) updateIterator.next();
                int index = list.indexOf(serverBattery);
                if (index != -1) {
                    // We replace the element in the list :o
                    serverBattery.setID(list.get(index).getId());
                    list.remove(index);
                    list.add(index, serverBattery);
                    // We don't forget to update the DB
                    updateList.add(serverBattery);
                } else {
                    //It's a new battery
                    insertList.add(serverBattery);
                }
            }
            new ASyncTaskUpdateDB().execute(context, updateList, insertList);

        }
        sortBatteries();
    }

    public void sortBatteries() {
        Collections.sort(list, new Comparator<Battery>() {
            @Override
            public int compare(Battery battery, Battery t1) {
                int cellsBatt = battery.getNbS();
                int cellsT1 = t1.getNbS();

                if (cellsBatt < cellsT1)
                    return -1;
                else if (cellsBatt == cellsT1)
                    return 0;
                else
                    return 1;
            }
        });
        notifyListeners();
    }

    public static Battery getBatteryFromJSON(JSONObject jsonObject) {
        Battery batt = new Battery();
        String name = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            Date date = format.parse(jsonObject.getString("bought"));
            name = jsonObject.getString("name");

            batt.setName(name);
            batt.setPurchaseDate(date);
            batt.setServer_id(jsonObject.getString("_id"));
            batt.setNbOfCycles(jsonObject.getInt("cycles"));
            batt.setBrand(jsonObject.getString("brand"));
            batt.setModel(jsonObject.getString("model"));
            batt.setNbS(jsonObject.getInt("cells"));
            batt.setCapacity(jsonObject.getInt("capacity"));

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
            return null;
        }
        return batt;
    }
    private void addBatteryFromJSONOBject(JSONObject jsonObject) {
        String name = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            Date date = format.parse(jsonObject.getString("bought"));
            name = jsonObject.getString("name");
            Battery batt = new Battery();
            batt.setName(name);
            batt.setPurchaseDate(date);
            batt.setServer_id(jsonObject.getString("_id"));
            batt.setNbOfCycles(jsonObject.getInt("cycles"));
            batt.setBrand(jsonObject.getString("brand"));
            batt.setModel(jsonObject.getString("model"));
            batt.setNbS(jsonObject.getInt("cells"));
            batt.setCapacity(jsonObject.getInt("capacity"));
            this.insertBattery(batt);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

    }
}