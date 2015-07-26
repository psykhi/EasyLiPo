package com.theboredengineers.easylipo.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.theboredengineers.easylipo.objects.Battery;
import com.theboredengineers.easylipo.objects.NfcTag;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by benoit.hocquet on 05/01/2015.
 * Helper class that makes the requests for batteries on the database
 */
public class BatterySQLite {

    SQLiteDatabase db = null;
    Context context = null;
    EasyLipoSQLite sql;
    /**
     * Constructor
     *
     * @param context the context used to open the database
     */
    public BatterySQLite(Context context) {

        sql = new EasyLipoSQLite(context);
        db = sql.getWritableDatabase();
        if (!db.isOpen())
            Log.e("Database", "Could not open database " + db.toString());
    }


    /**
     * Closes the opened database
     */
    public void close() {
        if (db != null)
            if (db.isOpen())
                db.close();
    }


    /**
     * Insert a new battery in the database.
     *
     * @param battery the battery to be inserted (its id will be modified if succeeded).
     * @return -1 if failed, the battery id if succeeded.
     */
    public int insertBattery(Battery battery) {
        int id;
        db = sql.getWritableDatabase();
        ContentValues values = new ContentValues();
        // if(db.isOpen())
        //   db.close();
        //db = sql.getWritableDatabase();
        NfcTag tag = battery.getTagID();
        values.put(EasyLipoSQLite.COL_BAT_SERVER_ID, battery.getServer_id());
        values.put(EasyLipoSQLite.COL_BAT_NAME, battery.getName());
        values.put(EasyLipoSQLite.COL_BAT_BRAND, battery.getBrand());
        values.put(EasyLipoSQLite.COL_BAT_MODEL, battery.getModel());
        values.put(EasyLipoSQLite.COL_BAT_CAPACITY, battery.getCapacity());
        values.put(EasyLipoSQLite.COL_BAT_CHARGERATE, battery.getChargeRate());
        values.put(EasyLipoSQLite.COL_BAT_DISCHARGERATE, battery.getDischargeRate());
        if(tag!=null)
            values.put(EasyLipoSQLite.COL_BAT_TAGID, tag.toString());
        values.put(EasyLipoSQLite.COL_BAT_RATING, battery.getRating());
        values.put(EasyLipoSQLite.COL_BAT_NBS, battery.getNbS());
        values.put(EasyLipoSQLite.COL_BAT_NBP, battery.getNbP());

        int timeOfPurchase;
        if (battery.getPurchaseDate() != null)
            timeOfPurchase = (int) battery.getPurchaseDate().getTime();
        else
            timeOfPurchase = -1;
        values.put(EasyLipoSQLite.COL_BAT_PURCHASEDATE, timeOfPurchase);
        values.put(EasyLipoSQLite.COL_BAT_NBOFCYCLES, battery.getNbOfCycles());

        id = -1;
        Battery temp = getBatteryByServerId(battery.getServer_id());
        if(temp != null)
        {
            db.update(EasyLipoSQLite.TABLE_BATTERIES, values, EasyLipoSQLite.COL_BAT_LOCAL_ID + "=" + battery.getId(), null);
            id =temp.getId();
        }
        else
        if (battery.getId() == -1) {
            id = (int) db.insert(EasyLipoSQLite.TABLE_BATTERIES, "null", values);
            battery = getBatteryById(id);
            Log.d("battery", battery.toString());
            battery.setID(id);
        } else
            db.update(EasyLipoSQLite.TABLE_BATTERIES, values, EasyLipoSQLite.COL_BAT_LOCAL_ID + "=" + battery.getId(), null);
        return id;
    }


    /**
     * Use exclusively this array as columns for query that will be treated
     * with the @getBatteryByLocalId method
     */
    private static final String ColumnsForRequest[] = new String[]{EasyLipoSQLite.COL_BAT_LOCAL_ID,
            EasyLipoSQLite.COL_BAT_SERVER_ID,
            EasyLipoSQLite.COL_BAT_NAME,
            EasyLipoSQLite.COL_BAT_BRAND,
            EasyLipoSQLite.COL_BAT_MODEL,
            EasyLipoSQLite.COL_BAT_CAPACITY,
            EasyLipoSQLite.COL_BAT_CHARGERATE,
            EasyLipoSQLite.COL_BAT_DISCHARGERATE,
            EasyLipoSQLite.COL_BAT_TAGID,
            EasyLipoSQLite.COL_BAT_RATING,
            EasyLipoSQLite.COL_BAT_PURCHASEDATE,
            EasyLipoSQLite.COL_BAT_NBOFCYCLES,
            EasyLipoSQLite.COL_BAT_NBS,
            EasyLipoSQLite.COL_BAT_NBP};


    /**
     * Get a battery in database
     *
     * @param id the id of the battery to be retrieved
     * @return the found battery, or null otherwise.
     */
    public Battery getBatteryById(int id) {
        Cursor c = db.query(EasyLipoSQLite.TABLE_BATTERIES,
                ColumnsForRequest,
                EasyLipoSQLite.COL_BAT_LOCAL_ID + " = " + Integer.toString(id),
                null, null, null, null);

        List<Battery> batteryList;

        batteryList = cursorToBatteries(c);
        if (batteryList != null && batteryList.size() > 0)
            return batteryList.get(0);
        else
            return null;
    }

    public Battery getBatteryByServerId(String id) {
        if(!id.equals("")) {
            Cursor c = db.query(EasyLipoSQLite.TABLE_BATTERIES,
                    ColumnsForRequest,
                    EasyLipoSQLite.COL_BAT_SERVER_ID + " = \"" + id + "\"",
                    null, null, null, null);

            List<Battery> batteryList;

            batteryList = cursorToBatteries(c);
            if (batteryList != null && batteryList.size() > 0)
                return batteryList.get(0);
            else
                return null;
        }
        else
            return null;
    }

    /**
     * Remove all the batteries from database
     */
    public void removeAllBatteries() {
        db = sql.getWritableDatabase();
        Log.e("DB",db.isDbLockedByCurrentThread()+" locked "+db.isReadOnly()+" RO ");
        db.delete(EasyLipoSQLite.TABLE_BATTERIES, null, null);
    }


    /**
     * Get a battery from its tag
     *
     * @param tag the tag of the battery
     * @return the found battery, null otherwise.
     */
    public Battery getBatteryByTag(NfcTag tag) {
        if (tag == null)
            return null;
        String tagAsString = tag.toString();
        Cursor c = db.query(EasyLipoSQLite.TABLE_BATTERIES,
                ColumnsForRequest,
                EasyLipoSQLite.COL_BAT_TAGID + " = " + "\"" + tagAsString + "\"",
                null, null, null, null);

        List<Battery> batteryList;
        batteryList = cursorToBatteries(c);
        if (batteryList != null && batteryList.size() > 0)
            return batteryList.get(0);
        else
            return null;
    }


    /**
     * Get Batteries sorted by capacity for any given S, P
     *
     * @return the found batteries
     */
    public ArrayList<Battery> getBatteriesByCapacity() {
        return getBatteries(null, null, null, null,
                EasyLipoSQLite.COL_BAT_NBS + ", "
                        + EasyLipoSQLite.COL_BAT_NBP + ", "
                        + EasyLipoSQLite.COL_BAT_CAPACITY + " ASC");
    }


    /**
     * Get Batteries sorted by name
     *
     * @return the batteries
     */
    public ArrayList<Battery> getBatteriesByName() {
        return getBatteries(null, null, null, null, EasyLipoSQLite.COL_BAT_NAME + " ASC");
    }


    /**
     * Perform a database query on batteries
     *
     * @param selection     SELECT clause exemple : select = "ColumnName=?"
     * @param selectionArgs Select args (replace the "?" on the selection argument) ONLY FOR STRINGS
     * @param groupBy       GROUPBY clause
     * @param having
     * @param orderBy       ORDERBY clause
     * @return
     */
    private ArrayList<Battery> getBatteries(String selection,
                                            String selectionArgs[],
                                            String groupBy,
                                            String having,
                                            String orderBy) {
        Cursor c = db.query(EasyLipoSQLite.TABLE_BATTERIES,
                ColumnsForRequest,
                selection, //selection
                selectionArgs, //selectionArgs
                groupBy, //groupBy
                having, //having
                orderBy); //orderBy

        ArrayList<Battery> batteryList;
        batteryList = cursorToBatteries(c);

        return batteryList;
    }


    /**
     * Get all batteries that are inserted in the database
     *
     * @return the list of batteries from database
     */
    public ArrayList<Battery> getAllBatteries() {
        Cursor c = db.query(EasyLipoSQLite.TABLE_BATTERIES,
                ColumnsForRequest,
                null, //no condition
                null, null, null, null);

        ArrayList<Battery> batteryList;
        batteryList = cursorToBatteries(c);

        return batteryList;
    }

    public boolean removeBattery(int id){
        db = sql.getWritableDatabase();
        int i = db.delete(EasyLipoSQLite.TABLE_BATTERIES,
                EasyLipoSQLite.COL_BAT_LOCAL_ID + "=" + id,
                null);
        if(i==1)
            return true;
        else
            return false;
    }

    public boolean removeBattery(NfcTag nfcTag){
        db = sql.getWritableDatabase();
        int i = db.delete(EasyLipoSQLite.TABLE_BATTERIES,
                EasyLipoSQLite.COL_BAT_TAGID + "=?" , //string : pass arg as 3rd arg of delete func
                new String[]{nfcTag.toString()});
        if(i==1)
            return true;
        else
            return false;
    }

    /**
     * Get a list of batteries from a cursor object
     *
     * @param c the cursor object, must be created with a query using ColumnsForRequest as columns.
     * @return the list of batteries
     */
    private ArrayList<Battery> cursorToBatteries(Cursor c) {
        if (c == null)
            return null;
        ArrayList<Battery> batteries = new ArrayList<>();
        Battery battery;
        if (c.getCount() == 0)
            return batteries;
        c.moveToFirst();
        do {
            String nfcTagString;
            String name, brand, model,server_id;
            int id, capacity, chargeRate, dischargeRate, nbOfCycles, rating, dateInt,
                    nbS, nbP;
            id = c.getInt(0);
            server_id = c.getString(1);
            name = c.getString(2);
            brand = c.getString(3);
            model = c.getString(4);
            capacity = c.getInt(5);
            chargeRate = c.getInt(6);
            dischargeRate = c.getInt(7);
            nfcTagString = c.getString(8);
            rating = c.getInt(9);
            dateInt = c.getInt(10);
            nbOfCycles = c.getInt(11);
            nbS = c.getInt(12);
            nbP = c.getInt(13);
            NfcTag tag = NfcTag.BuildFromString(nfcTagString);
            Date date = null;
            if (dateInt != -1)
                date = new Date(dateInt);

            battery = new Battery();
            battery.setServer_id(server_id);
            battery.setName(name);
            battery.setTagID(tag);
            battery.setID(id);
            battery.setBrand(brand);
            battery.setModel(model);
            battery.setCapacity(capacity);
            battery.setChargeRate(chargeRate);
            battery.setDischargeRate(dischargeRate);
            battery.setRating(rating);
            battery.setPurchaseDate(date);
            battery.setNbOfCycles(nbOfCycles);
            battery.setNbS(nbS);
            battery.setNbP(nbP);

            batteries.add(battery);

            c.moveToNext();
        }
        while (!c.isAfterLast());
        c.close();
        return batteries;
    }

}
