package com.theboredengineers.easylipo.network;

import com.theboredengineers.easylipo.network.command.NewBatteryCommand;

import java.util.ArrayList;

/**
 * Created by Alex on 25/07/2015.
 */
public class PendingCommands{

    private static ArrayList<NetworkCommand> commands = new ArrayList<NetworkCommand>();



    public static void processAll(final NetworkSyncListener l)
    {
        if (commands.size() != 0)
        {
            commands.get(0).execute(new NetworkCommandListener() {
                @Override
                public void onNetworkTaskEnd(Boolean success, Object json) {
                    if(success)
                    {
                        commands.remove(0);
                        processAll(l);
                    }
                }
            });
        }
        else
        {
            l.onNetworkSyncEnded(true);
        }
    }

    public static void add(NetworkCommand command) {
        commands.add(command);
    }
}
