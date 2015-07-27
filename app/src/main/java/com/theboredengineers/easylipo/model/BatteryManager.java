package com.theboredengineers.easylipo.model;

import android.content.Context;
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
import java.util.Date;
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
        for (int i = 0; i < listeners.size() ; i++) {
            listeners.get(i).onBatteryListChanged();
        }
    }
    /**
     * Refreshes the battery list with the current batteries
     */
    private void refreshList() {
        //TODO sharedpreferences
        sqlBat.getBatteriesByCapacity(list);
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
    public int insertOrUpdateBattery(Battery battery) {
        Log.d("Insert", battery.toString());
        int ret = sqlBat.insertBattery(battery);
        refreshList();
        return ret;
    }

    public Boolean addCycle(Battery battery)
    {
        battery.setNbOfCycles(battery.getNbOfCycles() + 1);
        if(insertOrUpdateBattery(battery)!= -1)
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
    public boolean removeBattery(int id) {
        boolean returnCode =  sqlBat.removeBattery(id);
        if(returnCode)
            refreshList();
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

    public void setBatteries(Object json) {

        removeAllBatteries();
        if (json instanceof JSONObject)
        {

        }
        else if (json instanceof JSONArray)
        {
            JSONArray array = (JSONArray) json;

            for (int i = 0; i < array.length(); i++) {
                try {
                    addBatteryFromJSONOBject(array.getJSONObject(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
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
            this.insertOrUpdateBattery(batt);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

    }
}