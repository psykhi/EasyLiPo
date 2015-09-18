package com.theboredengineers.easylipo.network;

import android.content.Context;
import android.util.Log;

import com.theboredengineers.easylipo.network.listeners.NetworkCommandListener;
import com.theboredengineers.easylipo.network.server.RemoteServer;

import org.json.JSONObject;

/**
 * Created by Alex on 16/06/2015.
 */
public abstract class NetworkCommand {
    private NetworkCommandListener commandListener;
    private static final String TAG = "Network command";
    private long id;
    protected long batteryId = -1;

    public NetworkCommand()
    {
    }
    public abstract String getMethod();

    public abstract String getRoute();

    public JSONObject getJSON() {
        return null;
    }

    public abstract String getName();

    public NetworkCommandListener getListener() {
        return commandListener;
    }

    public void execute(Context context, NetworkCommandListener listener)
    {

        Log.d("Commands", "Executing " + getName());
        this.commandListener = listener;
        if (NetworkManager.isConnected(context)) {
            new NetworkTask().execute(context, this);
        } else {
            listener.onNetworkTaskEnd(false, RemoteServer.formatErrorMessageJSON("No Internet connectivity."));
        }
    }


    public void onSessionIDCookie(String cookie) {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBatteryLocalId() {
        return batteryId;
    }
}
