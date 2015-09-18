package com.theboredengineers.easylipo.network.command;

import com.theboredengineers.easylipo.network.NetworkCommand;
import com.theboredengineers.easylipo.network.server.RemoteServer;
import com.theboredengineers.easylipo.objects.Battery;

import org.json.JSONObject;

/**
 * Created by Alex on 25/07/2015.
 */
public class NewBatteryCommand extends NetworkCommand {
    private final Battery battery;

    public NewBatteryCommand(Battery b) {
        this.battery = b;
        this.batteryId = b.getLocalId();
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public String getRoute() {
        return RemoteServer.Routes.NEW_BATTERY;
    }

    @Override
    public String getName() {
        return "New battery";
    }

    @Override
    public JSONObject getJSON() {
        return RemoteServer.formatNewBatteryParameters(battery.getBrand(),
                battery.getModel(),
                battery.getNbS(),
                battery.getCapacity(),
                battery.getNbOfCycles(),
                battery.getDischargeRate(),
                battery.getChargeRate(),
                battery.getPurchaseDate(),
                battery.isCharged(),
                battery.getName());
    }
}
