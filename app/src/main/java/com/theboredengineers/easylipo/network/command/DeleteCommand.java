package com.theboredengineers.easylipo.network.command;

import com.theboredengineers.easylipo.network.NetworkCommand;
import com.theboredengineers.easylipo.network.server.RemoteServer;

/**
 * Created by Alexandre on 29/07/2015.
 */
public class DeleteCommand extends NetworkCommand {


    private final String serverId;

    public DeleteCommand(String serverId) {
        this.serverId = serverId;
    }

    @Override
    public String getMethod() {
        return "DELETE";
    }

    @Override
    public String getRoute() {
        return RemoteServer.Routes.DELETE_BATTERY + "/" + serverId;
    }

    @Override
    public String getName() {
        return "Delete battery";
    }
}
