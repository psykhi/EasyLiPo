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

/**
 * Created by benoit.hocquet on 05/01/2015.
 * Helper class that makes the requests for batteries on the database
 */
public class BatterySQLite {

    private static final String TAG = "BatterySQLite";

    SQLiteDatabase db = null;
    EasyLipoSQLite sql;
    /**
     * Constructor
     *
     * @param context the context used to open the database
     */
    public BatterySQLite(Context context) {

        sql = EasyLipoSQLite.getInstance(context);
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
    public int updateBattery(Battery battery) {
        int id;
        db = sql.getWritableDatabase();
        ContentValues values = new ContentValues();
        // if(db.isOpen())
        //   db.close();
        //db = sql.getWritableDatabase();
        NfcTag tag = battery.getTagID();
        values.put(EasyLipoSQLite.Batteries.COL_BAT_SERVER_ID, battery.getServerId());
        values.put(EasyLipoSQLite.Batteries.COL_BAT_NAME, battery.getName());
        values.put(EasyLipoSQLite.Batteries.COL_BAT_BRAND, battery.getBrand());
        values.put(EasyLipoSQLite.Batteries.COL_BAT_MODEL, battery.getModel());
        values.put(EasyLipoSQLite.Batteries.COL_BAT_CAPACITY, battery.getCapacity());
        values.put(EasyLipoSQLite.Batteries.COL_BAT_CHARGERATE, battery.getChargeRate());
        values.put(EasyLipoSQLite.Batteries.COL_BAT_DISCHARGERATE, battery.getDischargeRate());
        if(tag!=null)
            values.put(EasyLipoSQLite.Batteries.COL_BAT_TAGID, tag.toString());
        values.put(EasyLipoSQLite.Batteries.COL_BAT_RATING, battery.getRating());
        values.put(EasyLipoSQLite.Batteries.COL_BAT_NBS, battery.getNbS());
        values.put(EasyLipoSQLite.Batteries.COL_BAT_NBP, battery.getNbP());
        values.put(EasyLipoSQLite.Batteries.COL_BAT_CHARGED, battery.isCharged());

        long timeOfPurchase;
        if (battery.getPurchaseDate() != null)
            timeOfPurchase = battery.getPurchaseDate().getTime();
        else
            timeOfPurchase = -1;
        values.put(EasyLipoSQLite.Batteries.COL_BAT_PURCHASEDATE, timeOfPurchase);
        values.put(EasyLipoSQLite.Batteries.COL_BAT_NBOFCYCLES, battery.getNbOfCycles());

        id = -1;
        Battery temp = getBatteryByServerId(battery.getServerId());
        if(temp != null)
        {
            db.update(EasyLipoSQLite.Batteries.TABLE_BATTERIES, values,
                    EasyLipoSQLite.Batteries.COL_BAT_LOCAL_ID + "=" + battery.getLocalId(), null);
            id = temp.getLocalId();
        }
        else
            db.update(EasyLipoSQLite.Batteries.TABLE_BATTERIES, values,
                    EasyLipoSQLite.Batteries.COL_BAT_LOCAL_ID + "=" + battery.getLocalId(), null);
        return id;
    }

    public int insertBattery(Battery battery) {
        int id;
        db = sql.getWritableDatabase();
        ContentValues values = new ContentValues();
        // if(db.isOpen())
        //   db.close();
        //db = sql.getWritableDatabase();
        NfcTag tag = battery.getTagID();
        values.put(EasyLipoSQLite.Batteries.COL_BAT_SERVER_ID, battery.getServerId());
        values.put(EasyLipoSQLite.Batteries.COL_BAT_NAME, battery.getName());
        values.put(EasyLipoSQLite.Batteries.COL_BAT_BRAND, battery.getBrand());
        values.put(EasyLipoSQLite.Batteries.COL_BAT_MODEL, battery.getModel());
        values.put(EasyLipoSQLite.Batteries.COL_BAT_CAPACITY, battery.getCapacity());
        values.put(EasyLipoSQLite.Batteries.COL_BAT_CHARGERATE, battery.getChargeRate());
        values.put(EasyLipoSQLite.Batteries.COL_BAT_DISCHARGERATE, battery.getDischargeRate());
        if (tag != null)
            values.put(EasyLipoSQLite.Batteries.COL_BAT_TAGID, tag.toString());
        values.put(EasyLipoSQLite.Batteries.COL_BAT_RATING, battery.getRating());
        values.put(EasyLipoSQLite.Batteries.COL_BAT_NBS, battery.getNbS());
        values.put(EasyLipoSQLite.Batteries.COL_BAT_NBP, battery.getNbP());
        values.put(EasyLipoSQLite.Batteries.COL_BAT_CHARGED, battery.isCharged());

        long timeOfPurchase;
        if (battery.getPurchaseDate() != null)
            timeOfPurchase = battery.getPurchaseDate().getTime();
        else
            timeOfPurchase = -1;
        values.put(EasyLipoSQLite.Batteries.COL_BAT_PURCHASEDATE, timeOfPurchase);
        values.put(EasyLipoSQLite.Batteries.COL_BAT_NBOFCYCLES, battery.getNbOfCycles());
        id = (int) db.insert(EasyLipoSQLite.Batteries.TABLE_BATTERIES, "null", values);
        battery.setID(id);
        return id;
    }

    /**
     * Use exclusively this array as columns for query that will be treated
     * with the @getBatteryByLocalId method
     */
    private static final String ColumnsForRequest[] = new String[]{EasyLipoSQLite.Batteries.COL_BAT_LOCAL_ID,
            EasyLipoSQLite.Batteries.COL_BAT_SERVER_ID,
            EasyLipoSQLite.Batteries.COL_BAT_NAME,
            EasyLipoSQLite.Batteries.COL_BAT_BRAND,
            EasyLipoSQLite.Batteries.COL_BAT_MODEL,
            EasyLipoSQLite.Batteries.COL_BAT_CAPACITY,
            EasyLipoSQLite.Batteries.COL_BAT_CHARGERATE,
            EasyLipoSQLite.Batteries.COL_BAT_DISCHARGERATE,
            EasyLipoSQLite.Batteries.COL_BAT_TAGID,
            EasyLipoSQLite.Batteries.COL_BAT_RATING,
            EasyLipoSQLite.Batteries.COL_BAT_PURCHASEDATE,
            EasyLipoSQLite.Batteries.COL_BAT_NBOFCYCLES,
            EasyLipoSQLite.Batteries.COL_BAT_NBS,
            EasyLipoSQLite.Batteries.COL_BAT_NBP,
            EasyLipoSQLite.Batteries.COL_BAT_CHARGED};


    /**
     * Get a battery in database
     *
     * @param id the id of the battery to be retrieved
     * @return the found battery, or null otherwise.
     */
    public Battery getBatteryById(int id) {
        Cursor c = db.query(EasyLipoSQLite.Batteries.TABLE_BATTERIES,
                ColumnsForRequest,
                EasyLipoSQLite.Batteries.COL_BAT_LOCAL_ID + " = " + Integer.toString(id),
                null, null, null, null);

        ArrayList<Battery> batteryList = new ArrayList<>();

        batteryList = cursorToBatteries(c, batteryList);

        if (batteryList != null && batteryList.size() > 0)
            return batteryList.get(0);
        else
            return null;
    }

    public Battery getBatteryByServerId(String id) {
        if(!id.equals("")) {
            Cursor c = db.query(EasyLipoSQLite.Batteries.TABLE_BATTERIES,
                    ColumnsForRequest,
                    EasyLipoSQLite.Batteries.COL_BAT_SERVER_ID + " = \"" + id + "\"",
                    null, null, null, null);

            ArrayList<Battery> batteryList = new ArrayList<>();

            cursorToBatteries(c, batteryList);
            if (batteryList.size() > 0)
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
        db.delete(EasyLipoSQLite.Batteries.TABLE_BATTERIES, null, null);
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
        Cursor c = db.query(EasyLipoSQLite.Batteries.TABLE_BATTERIES,
                ColumnsForRequest,
                EasyLipoSQLite.Batteries.COL_BAT_TAGID + " = " + "\"" + tagAsString + "\"",
                null, null, null, null);

        ArrayList<Battery> batteryList = new ArrayList<>();

        batteryList = cursorToBatteries(c, batteryList);
        if (batteryList != null && batteryList.size() > 0)
            return batteryList.get(0);
        else
            return null;

    }


    /**
     * Get Batteries sorted by capacity for any given S, P
     *
     *
     */
    public void getBatteriesByCapacity(ArrayList<Battery> list) {
        getBatteries(null, null, null, null,
                EasyLipoSQLite.Batteries.COL_BAT_NBS + ", "
                        + EasyLipoSQLite.Batteries.COL_BAT_NBP + ", "
                        + EasyLipoSQLite.Batteries.COL_BAT_CAPACITY + " ASC", list);
    }


    /**
     * Perform a database query on batteries
     *
     * @param selection     SELECT clause exemple : select = "ColumnName=?"
     * @param selectionArgs Select args (replace the "?" on the selection argument) ONLY FOR STRINGS
     * @param groupBy       GROUPBY clause
     * @param having        having
     * @param orderBy       ORDERBY clause
     */
    private void getBatteries(String selection,
                              String selectionArgs[],
                              String groupBy,
                              String having,
                              String orderBy, ArrayList<Battery> list) {
        Cursor c = db.query(EasyLipoSQLite.Batteries.TABLE_BATTERIES,
                ColumnsForRequest,
                selection, //selection
                selectionArgs, //selectionArgs
                groupBy, //groupBy
                having, //having
                orderBy); //orderBy

        cursorToBatteries(c, list);

    }


    /**
     * Get all batteries that are inserted in the database
     *
     */
    public void getAllBatteries(ArrayList<Battery> batteryList) {
        Cursor c = db.query(EasyLipoSQLite.Batteries.TABLE_BATTERIES,
                ColumnsForRequest,
                null, //no condition
                null, null, null, null);

        cursorToBatteries(c, batteryList);
    }

    public boolean removeBattery(int id){
        db = sql.getWritableDatabase();
        int i = db.delete(EasyLipoSQLite.Batteries.TABLE_BATTERIES,
                EasyLipoSQLite.Batteries.COL_BAT_LOCAL_ID + "=" + id,
                null);
        return i == 1;
    }

    public boolean removeBattery(NfcTag nfcTag){
        db = sql.getWritableDatabase();
        int i = db.delete(EasyLipoSQLite.Batteries.TABLE_BATTERIES,
                EasyLipoSQLite.Batteries.COL_BAT_TAGID + "=?", //string : pass arg as 3rd arg of delete func
                new String[]{nfcTag.toString()});
        return i == 1;
    }

    /**
     * Get a list of batteries from a cursor object
     *
     * @param c the cursor object, must be created with a query using ColumnsForRequest as columns.
     * @return the list of batteries
     */
    private ArrayList<Battery> cursorToBatteries(Cursor c, ArrayList<Battery> batteries) {
        if (c == null)
            return null;
//        ArrayList<Battery> batteries = new ArrayList<>();
        batteries.clear();
        Battery battery;
        if (c.getCount() == 0)
            return batteries;
        c.moveToFirst();
        do {
            String nfcTagString;
            String name, brand, model,server_id;
            int id;
            int capacity;
            int chargeRate;
            int dischargeRate;
            int nbOfCycles;
            int rating;
            long dateInt;
            int nbS;
            int nbP;
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
            dateInt = c.getLong(10);
            nbOfCycles = c.getInt(11);
            nbS = c.getInt(12);
            nbP = c.getInt(13);
            boolean charged;
            charged = c.getInt(14) == 1;
            NfcTag tag = NfcTag.BuildFromString(nfcTagString);
            Date date = null;
            Log.d(TAG, "Date int " + dateInt);
            if (dateInt != -1)
                date = new Date(dateInt);

            battery = new Battery();
            battery.setServerId(server_id);
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
            battery.setCharged(charged);

            batteries.add(battery);

            c.moveToNext();
        }
        while (!c.isAfterLast());
        c.close();
        return batteries;
    }

}
