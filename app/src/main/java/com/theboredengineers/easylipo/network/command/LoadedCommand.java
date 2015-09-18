package com.theboredengineers.easylipo.network.command;

import com.theboredengineers.easylipo.network.NetworkCommand;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alexandre on 16/08/2015.
 */
public class LoadedCommand extends NetworkCommand {
    private String name;
    private String route;
    private String method;
    private String jsonString;


    public LoadedCommand(String name, String route, String method, String json) {
        this.route = route;
        this.name = name;
        this.jsonString = json;
        this.method = method;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getRoute() {
        return route;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public JSONObject getJSON() {
        JSONObject json = null;
        try {
            json = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
