package com.theboredengineers.easylipo.network.command;

import com.theboredengineers.easylipo.network.NetworkCommand;
import com.theboredengineers.easylipo.network.server.RemoteServer;

import org.json.JSONObject;

/**
 * Created by Alex on 25/07/2015.
 */
public class AddCycleCommand extends NetworkCommand{

    private final String serverID;

    public AddCycleCommand(String serverID)
    {
        this.serverID = serverID;
    }
    @Override
    public String getMethod() {
        return "PATCH";
    }

    @Override
    public String getRoute() {
        return RemoteServer.Routes.ADD_CYCLE+"/"+serverID;
    }

    @Override
    public String getName() {
        return "Add Cycle";
    }

    @Override
    public JSONObject getJSON() {
        return RemoteServer.formatAddCycleParameter(serverID, 1);
    }
}
