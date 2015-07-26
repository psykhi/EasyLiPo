package com.theboredengineers.easylipo.network.command;

import com.theboredengineers.easylipo.network.NetworkCommand;
import com.theboredengineers.easylipo.network.NetworkCommandListener;
import com.theboredengineers.easylipo.network.server.RemoteServer;

/**
 * Created by Alex on 25/07/2015.
 */
public class SignoutCommand extends NetworkCommand {

    @Override
    public String getMethod() {
        return "GET";
    }

    @Override
    public String getRoute() {
        return RemoteServer.Routes.SIGNOUT;
    }

    @Override
    public String getName() {
        return "Sign out";
    }
}
