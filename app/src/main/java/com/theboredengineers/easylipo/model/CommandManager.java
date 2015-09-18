package com.theboredengineers.easylipo.model;

import android.content.Context;

import com.theboredengineers.easylipo.network.NetworkCommand;
import com.theboredengineers.easylipo.network.listeners.NetworkCommandListener;
import com.theboredengineers.easylipo.network.listeners.NetworkSyncListener;
import com.theboredengineers.easylipo.network.server.RemoteServer;

import java.util.ArrayList;

/**
 * Created by Alex on 25/07/2015.
 */
public class CommandManager {

    private static ArrayList<NetworkCommand> commands = new ArrayList<NetworkCommand>();
    private static CommandsSQLite sql;
    private static CommandManager instance = null;

    private CommandManager(Context context) {
        sql = new CommandsSQLite(context);
        sql.loadCommandList(commands);
    }

    public static CommandManager getInstance(Context context)
    {
        if (instance == null)
            instance = new CommandManager(context);
        return instance;
    }

    public void processAll(final Context context, final NetworkSyncListener l) {
        if (commands.size() != 0)
        {
            commands.get(0).execute(context, new NetworkCommandListener() {
                @Override
                public void onNetworkTaskEnd(Boolean success, Object json) {
                    if(success)
                    {
                        sql.delete(commands.get(0));
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

    public void add(NetworkCommand command) {
        commands.add(command);
        sql.insert(command);
    }
}
