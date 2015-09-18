package com.theboredengineers.easylipo.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.theboredengineers.easylipo.network.NetworkCommand;
import com.theboredengineers.easylipo.network.command.LoadedCommand;
import com.theboredengineers.easylipo.network.command.NewBatteryCommand;
import com.theboredengineers.easylipo.objects.Battery;

import java.util.ArrayList;

/**
 * Created by Alexandre on 05/08/2015.
 */
public class CommandsSQLite {

    private static final String TAG = "NetworkCommandsSQL";

    private static final int COL_ID = 0;
    private static final int COL_JSON = 1;
    private static final int COL_METHOD = 2;
    private static final int COL_NAME = 3;
    private static final int COL_ROUTE = 4;
    private static final int COL_BATTERY_LOCAL_ID = 5;

    private static final String ColumnsForRequest[] = new String[]{
            EasyLipoSQLite.Commands.COL_COMMAND_ID,
            EasyLipoSQLite.Commands.COL_COMMAND_JSON,
            EasyLipoSQLite.Commands.COL_COMMAND_METHOD,
            EasyLipoSQLite.Commands.COL_COMMAND_NAME,
            EasyLipoSQLite.Commands.COL_COMMAND_ROUTE,
            EasyLipoSQLite.Commands.COL_COMMAND_BATTERY_LOCAL_ID
    };
    private final Context context;
    private SQLiteDatabase db = null;
    private EasyLipoSQLite sql;

    /**
     * Constructor
     *
     * @param context the context used to open the database
     */
    public CommandsSQLite(Context context) {

        this.context = context;
        sql = EasyLipoSQLite.getInstance(context);
        db = sql.getWritableDatabase();
        if (!db.isOpen())
            Log.e(TAG, "Could not open database " + db.toString());
    }

    public long insert(NetworkCommand command) {
        ContentValues values = new ContentValues();

        values.put(EasyLipoSQLite.Commands.COL_COMMAND_JSON, command.getJSON().toString());
        values.put(EasyLipoSQLite.Commands.COL_COMMAND_NAME, command.getName());
        values.put(EasyLipoSQLite.Commands.COL_COMMAND_ROUTE, command.getRoute());
        values.put(EasyLipoSQLite.Commands.COL_COMMAND_METHOD, command.getMethod());
        values.put(EasyLipoSQLite.Commands.COL_COMMAND_BATTERY_LOCAL_ID, command.getBatteryLocalId());
        return db.insert(EasyLipoSQLite.Commands.TABLE_COMMANDS, "null", values);
    }

    public boolean delete(NetworkCommand command) {
        int rows = db.delete(EasyLipoSQLite.Commands.TABLE_COMMANDS, EasyLipoSQLite.Commands.COL_COMMAND_ID +
                "=" + command.getId(), null);
        return rows == 1;
    }


    public void loadCommandList(ArrayList<NetworkCommand> list) {

        Cursor c = db.query(EasyLipoSQLite.Commands.TABLE_COMMANDS,
                ColumnsForRequest,
                null, //selection
                null, //selectionArgs
                null, //groupBy
                null, //having
                EasyLipoSQLite.Commands.COL_COMMAND_ID + " ASC"); //orderBy

        cursorToCommandList(c, list);
    }

    private void cursorToCommandList(Cursor c, ArrayList<NetworkCommand> list) {
        if (c == null)
            return;
        if (c.getCount() == 0)
            return;
        c.moveToFirst();
        do {
            NetworkCommand command;
            final String method = c.getString(COL_METHOD);
            /* We look at the type of command (POST implies a battery creation, which
            means we need to create a NewBattteryCommand and associate it with a local battery
             */
            if (method.equals("POST")) {
                long batteryId = c.getLong(COL_BATTERY_LOCAL_ID);
                Battery batt = BatteryManager.getInstance(context).getBatteryByLocalId((int) batteryId);
                command = new NewBatteryCommand(batt);
            } else {
                command = new LoadedCommand(c.getString(COL_NAME), c.getString(COL_ROUTE),
                        method, c.getString(COL_JSON));
            }

            list.add(command);
            c.moveToNext();
        } while (!c.isAfterLast());
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
