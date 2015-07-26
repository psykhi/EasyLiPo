package com.theboredengineers.easylipo.network;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by Alex on 16/06/2015.
 */
public abstract class NetworkCommand {
    private NetworkCommandListener commandListener;

    public NetworkCommand()
    {
    }
    public abstract String getMethod();

    public abstract String getRoute();

    public String customRoute(){return "";};
    public JSONObject getJSON() {
        return null;
    }

    public abstract String getName();

    public NetworkCommandListener getListener() {
        return commandListener;
    }

    public void execute(NetworkCommandListener listener)
    {
        Log.d("Commands", "Executing command " + getName());
        this.commandListener = listener;
        new NetworkTask().execute(this);
    }


    public void onSessionIDCookie(String cookie) {

    }
}
