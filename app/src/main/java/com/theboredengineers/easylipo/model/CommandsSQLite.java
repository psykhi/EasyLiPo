package com.theboredengineers.easylipo.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Alexandre on 05/08/2015.
 */
public class CommandsSQLite {

    private static final String TAG = "CommandsSQL";

    SQLiteDatabase db = null;
    EasyLipoSQLite sql;

    /**
     * Constructor
     *
     * @param context the context used to open the database
     */
    public CommandsSQLite(Context context) {

        sql = new EasyLipoSQLite(context);
        db = sql.getWritableDatabase();
        if (!db.isOpen())
            Log.e(TAG, "Could not open database " + db.toString());
    }


    /**
     * Closes the opened database
     */
    public void close() {
        if (db != null)
            if (db.isOpen())
                db.close();
    }

}
