package com.theboredengineers.easylipo.network.command;

import com.theboredengineers.easylipo.network.NetworkCommand;
import com.theboredengineers.easylipo.network.NetworkCommandListener;
import com.theboredengineers.easylipo.network.server.RemoteServer;

import org.json.JSONObject;

/**
 * Created by Alex on 16/06/2015.
 */
public class GetBatteriesCommand extends NetworkCommand {


    @Override
    public String getMethod() {
        return "GET";
    }

    @Override
    public String getRoute() {
        return RemoteServer.Routes.GET_BATTERIES;
    }



    @Override
    public String getName() {
        return "Get user batteries";
    }
}
