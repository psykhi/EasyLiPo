package com.theboredengineers.easylipo.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by benoit.hocquet on 05/01/2015.
 * Class allowing user to access the database
 */
public class EasyLipoSQLite extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "easylipo.db";
    public static final int DATABASE_VERSION = 1;


    public static final String TABLE_BATTERIES = "table_batteries";
    public static final String COL_BAT_LOCAL_ID = "LOCAL_ID";
    public static final String COL_BAT_SERVER_ID = "SERVER_ID";
    public static final String COL_BAT_NAME = "Name";
    public static final String COL_BAT_BRAND = "brand";
    public static final String COL_BAT_MODEL = "model";
    public static final String COL_BAT_CAPACITY = "capacity";
    public static final String COL_BAT_CHARGERATE = "chargeRate";
    public static final String COL_BAT_DISCHARGERATE = "dischargeRate";
    public static final String COL_BAT_TAGID = "tagId";
    public static final String COL_BAT_RATING = "rating";
    public static final String COL_BAT_PURCHASEDATE = "purchaseDate";
    public static final String COL_BAT_NBOFCYCLES = "nbOfCycles";
    public static final String COL_BAT_NBS = "nbS";
    public static final String COL_BAT_NBP = "nbP";
    public static final String COL_BAT_CHARGED = "charged";


    private static final String CREATE_BDD = "CREATE TABLE " + TABLE_BATTERIES + " ("
            + COL_BAT_LOCAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_BAT_SERVER_ID + " TEXT,"
            + COL_BAT_NAME + " TEXT NOT NULL, "
            + COL_BAT_BRAND + " TEXT, "
            + COL_BAT_MODEL + " TEXT, "
            + COL_BAT_CAPACITY + " INTEGER, "
            + COL_BAT_CHARGERATE + " INTEGER,"
            + COL_BAT_DISCHARGERATE + " INTEGER,"
            + COL_BAT_TAGID + " TEXT,"
            + COL_BAT_RATING + " INTEGER,"
            + COL_BAT_PURCHASEDATE + " INTEGER,"
            + COL_BAT_NBOFCYCLES + " INTEGER,"
            + COL_BAT_NBS + " INTEGER," //add in v2
            + COL_BAT_NBP + " INTEGER,"
            + COL_BAT_CHARGED + " INTEGER"
            + ");";

    public EasyLipoSQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



}

