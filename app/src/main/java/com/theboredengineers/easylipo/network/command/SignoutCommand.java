package com.theboredengineers.easylipo.network.command;

import android.content.Context;

import com.theboredengineers.easylipo.network.NetworkCommand;
import com.theboredengineers.easylipo.network.listeners.NetworkCommandListener;
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

    @Override
    public void execute(Context context, NetworkCommandListener listener) {
//        super.execute(listener);
        listener.onNetworkTaskEnd(true, null);
    }
}
