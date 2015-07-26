package com.theboredengineers.easylipo.network.command;

import com.theboredengineers.easylipo.network.NetworkCommand;
import com.theboredengineers.easylipo.network.server.RemoteServer;
import com.theboredengineers.easylipo.objects.Battery;

import org.json.JSONObject;

/**
 * Created by Alex on 26/07/2015.
 */
public class UpdateBatteryCommand extends NetworkCommand{

    private final Battery battery;


    public UpdateBatteryCommand(Battery battery)
    {
        this.battery = battery;

    }


    @Override
    public String getMethod() {
        return "PATCH";
    }

    @Override
    public String getRoute() {
        return RemoteServer.Routes.UPDATE_BATTERY+"/"+battery.getServer_id();
    }

    @Override
    public String getName() {
        return "Update battery";
    }

    @Override
    public JSONObject getJSON() {
        return RemoteServer.formatUpdateParameters(battery);
    }
}
