package com.theboredengineers.easylipo.network;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.theboredengineers.easylipo.model.BatteryManager;
import com.theboredengineers.easylipo.network.command.AddCycleCommand;
import com.theboredengineers.easylipo.network.command.DeleteCommand;
import com.theboredengineers.easylipo.network.command.GetBatteriesCommand;
import com.theboredengineers.easylipo.network.command.NewBatteryCommand;
import com.theboredengineers.easylipo.network.command.SigninCommand;
import com.theboredengineers.easylipo.network.command.SignoutCommand;
import com.theboredengineers.easylipo.network.command.SignupCommand;
import com.theboredengineers.easylipo.network.command.UpdateBatteryCommand;
import com.theboredengineers.easylipo.network.listeners.NetworkCommandListener;
import com.theboredengineers.easylipo.network.listeners.NetworkSyncListener;
import com.theboredengineers.easylipo.network.listeners.OnBatteryInsertedListener;
import com.theboredengineers.easylipo.network.server.RemoteServer;
import com.theboredengineers.easylipo.objects.Battery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Alex on 14/06/2015.
 */
public class NetworkManager implements NetworkSyncListener {
    private ArrayList<NetworkSyncListener> observers;
    private static NetworkManager instance = null;
    private boolean syncing = false;
    private static final String TAG = "Network";

    private NetworkManager()
    {
        observers = new ArrayList<>();
        observers.add(this);

    }

    public static NetworkManager getInstance()
    {
        if (instance == null)
            instance = new NetworkManager();
        return instance;
    }


    public void networkSync(final Context context,final NetworkSyncListener listener)
    {
        if(listener != null)
            observers.add(listener);
        if(!syncing) {
            syncing = true;
            PendingCommands.processAll(context, new NetworkSyncListener() {
                @Override
                public void onNetworkSyncEnded(Boolean success, String errorMessageFromJSON) {
                    if (success) {
                        new GetBatteriesCommand().execute(context, new NetworkCommandListener() {
                            @Override
                            public void onNetworkTaskEnd(Boolean success, Object json) {
                                if(success) {
                                    BatteryManager.getInstance(context).setBatteries(json);
                                    syncing = false;
                                    // TODO signal to all observers...

                                    if (listener != null)
                                        listener.onNetworkSyncEnded(true, null);
                                }
                                else
                                {
                                    syncing = false;
                                    if (listener != null)
                                        listener.onNetworkSyncEnded(success, RemoteServer.getErrorMessageFromJSON(json));
                                }
                            }
                        });
                    }
                    else
                    {
                        syncing = false;
                        if (listener != null)
                            listener.onNetworkSyncEnded(success, errorMessageFromJSON);
                    }
                }
            });
        }
    }


    public void attemptLogin(Context context, String username, String pwd, final NetworkCommandListener listener) {
        new SigninCommand(context, username, pwd).execute(context, listener);
    }

    @Override
    public void onNetworkSyncEnded(Boolean success, String errorMessageFromJSON)
    {
        syncing = false;
    }

    public void signup(String firstName, String lastName, String username,String email, String pwd, Context context, NetworkCommandListener activity) {
        new SignupCommand(firstName, lastName, email, username, pwd).execute(context, activity);
    }

    public void signout(Context context, NetworkCommandListener l) {
        new SignoutCommand().execute(context, l);
    }

    public void addNewBattery(final Context context, final Battery b, final OnBatteryInsertedListener listener) {
        final NewBatteryCommand command  = new NewBatteryCommand(b);

        command.execute(context, new NetworkCommandListener() {
            @Override
            public void onNetworkTaskEnd(Boolean success, Object json) {
                String serverID = null;
                if (success) {
                    Log.d("battery", json.toString());
                    try {
                        serverID = ((JSONObject) json).getString("_id");
                        b.setServer_id(serverID);
                        BatteryManager.getInstance(context).updateBatterySQL(b);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    PendingCommands.add(command);
                    Log.e(TAG, "failed to upload battery");
                }
                if (listener != null)
                    listener.onBatteryInserted(serverID);
            }
        });
    }

    public void addCycle(Context context, Battery battery) {
        if (!battery.isLocal()) {
            final AddCycleCommand command = new AddCycleCommand(battery.getServer_id());
            command.execute(context, new NetworkCommandListener() {
                @Override
                public void onNetworkTaskEnd(Boolean success, Object json) {
                    if (success) {
                        Log.d("battery", "OK");
                    } else {
                        PendingCommands.add(command);
                        Log.e("battery", "failed to upload battery");
                    }
                }
            });
        }
    }

    public void updateBattery(Context context, Battery battery) {

        final UpdateBatteryCommand command = new UpdateBatteryCommand(battery);
        command.execute(context, new NetworkCommandListener() {
            @Override
            public void onNetworkTaskEnd(Boolean success, Object json) {
                if (success) {
                    Log.d("battery", "OK");
                } else {
                    PendingCommands.add(command);
                    Log.e("battery", "failed to upload battery");
                }
                }
        });

    }

    public void removeBattery(Context context, String serverId) {

        final DeleteCommand command = new DeleteCommand(serverId);
        command.execute(context, new NetworkCommandListener() {
            @Override
            public void onNetworkTaskEnd(Boolean success, Object json) {
                if (success) {
                    Log.d("battery", "OK");
                } else {
                    PendingCommands.add(command);
                    Log.e("battery", "failed to delete battery");
                }
            }
        });
    }

    public static Boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }


}
