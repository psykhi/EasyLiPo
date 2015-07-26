package com.theboredengineers.easylipo.network;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.theboredengineers.easylipo.model.BatteryManager;
import com.theboredengineers.easylipo.network.command.AddCycleCommand;
import com.theboredengineers.easylipo.network.command.GetBatteriesCommand;
import com.theboredengineers.easylipo.network.command.NewBatteryCommand;
import com.theboredengineers.easylipo.network.command.SigninCommand;
import com.theboredengineers.easylipo.network.command.SignoutCommand;
import com.theboredengineers.easylipo.network.command.SignupCommand;
import com.theboredengineers.easylipo.network.command.UpdateBatteryCommand;
import com.theboredengineers.easylipo.objects.Battery;
import com.theboredengineers.easylipo.security.NetworkEventListener;
import com.theboredengineers.easylipo.ui.activities.SignUpActivity;

import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by Alex on 14/06/2015.
 */
public class NetworkManager implements NetworkSyncListener{
    private ArrayList<NetworkCommand> commands;
    private ArrayList<NetworkSyncListener> observers;
    private static NetworkManager instance = null;
    private boolean syncing = false;
    HttpURLConnection serverConnection;


    private NetworkManager()
    {
        observers = new ArrayList<NetworkSyncListener>();
        commands = new ArrayList<NetworkCommand>();
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
            PendingCommands.processAll(new NetworkSyncListener() {
                @Override
                public void onNetworkSyncEnded(Boolean success) {
                    if (success) {
                        new GetBatteriesCommand().execute(new NetworkCommandListener() {
                            @Override
                            public void onNetworkTaskEnd(Boolean success, Object json) {
                                if(success) {
                                    BatteryManager.getInstance(context).setBatteries(json);
                                    syncing = false;
                                    // TODO signal to all observers...

                                    if (listener != null)
                                        listener.onNetworkSyncEnded(success);
                                }
                                else
                                {
                                    syncing = false;
                                    if (listener != null)
                                        listener.onNetworkSyncEnded(success);
                                }
                            }
                        });
                    }
                    else
                    {
                        syncing = false;
                        if (listener != null)
                            listener.onNetworkSyncEnded(success);
                    }
                }
            });
        }
    }


    public void attemptLogin(String username, String pwd, final NetworkCommandListener listener) {
        new SigninCommand(username,pwd).execute(listener);
    }

    @Override
    public void onNetworkSyncEnded(Boolean success)
    {
        syncing = false;
    }

    public void signup(String firstName, String lastName, String username,String email, String pwd, Context context, NetworkCommandListener activity) {
        new SignupCommand(firstName,lastName,email,username,pwd).execute(activity);
    }

    public void signout(NetworkCommandListener l) {
        new SignoutCommand().execute(l);
    }

    public void addNewBattery(Battery b) {
        final NewBatteryCommand command  = new NewBatteryCommand(b);

        command.execute(new NetworkCommandListener() {
            @Override
            public void onNetworkTaskEnd(Boolean success, Object json) {
                if (success) {
                    Log.d("battery", json.toString());
                    //TODO update DB
                } else {
                    PendingCommands.add(command);
                    Log.e("battery", "failed to upload battery");
                }
            }
        });
    }

    public void addCycle(Battery battery) {
        final AddCycleCommand command = new AddCycleCommand(battery.getServer_id());
        command.execute(new NetworkCommandListener() {
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

    public void updateBattery(Battery battery) {
        final UpdateBatteryCommand command = new UpdateBatteryCommand(battery);
        command.execute(new NetworkCommandListener() {
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
