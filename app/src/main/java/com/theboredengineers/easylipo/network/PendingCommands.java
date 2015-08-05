package com.theboredengineers.easylipo.network;

import android.content.Context;

import com.theboredengineers.easylipo.network.listeners.NetworkCommandListener;
import com.theboredengineers.easylipo.network.listeners.NetworkSyncListener;
import com.theboredengineers.easylipo.network.server.RemoteServer;

import java.util.ArrayList;

/**
 * Created by Alex on 25/07/2015.
 */
public class PendingCommands{

    private static ArrayList<NetworkCommand> commands = new ArrayList<NetworkCommand>();


    public static void processAll(final Context context, final NetworkSyncListener l)
    {
        if (commands.size() != 0)
        {
            commands.get(0).execute(context, new NetworkCommandListener() {
                @Override
                public void onNetworkTaskEnd(Boolean success, Object json) {
                    if(success)
                    {
                        commands.remove(0);
                        processAll(context, l);
                    } else {
                        l.onNetworkSyncEnded(false, RemoteServer.getErrorMessageFromJSON(json));
                    }

                }
            });
        }
        else
        {
            l.onNetworkSyncEnded(true, null);
        }
    }

    public static void add(NetworkCommand command) {
        commands.add(command);
    }
}
