package com.theboredengineers.easylipo.network.server;


import com.google.android.gms.games.request.Requests;
import com.theboredengineers.easylipo.objects.Battery;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Alex on 14/06/2015.
 */
public  class RemoteServer{
    private static final String url="http://easylipo.theboredengineers.com";

    public static String getUrl()
    {
        return url+"/";
    }

    public static JSONObject formatSigninParameters(String user, String pwd) {
        JSONObject json = new JSONObject();


        try {
            json.put("username", user);
            json.put("password", pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * @brief Formats a JSON object for signup
     * @param user
     * @param pwd
     * @param firstName
     * @param lastName
     * @param email
     * @return
     */
    public static JSONObject formatSignupParameters(String user, String pwd,
                                                String firstName, String lastName, String email) {
        JSONObject json = new JSONObject();


        try {
            json.put("username", user);
            json.put("password", pwd);
            json.put("firstName", firstName);
            json.put("lastName", lastName);
            json.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject formatNewBatteryParameters(String brand, String model,int cells,int capacity, int cycles, String name)
    {
        JSONObject json = new JSONObject();


        try {
            json.put("brand", brand);
            json.put("model", model);
            json.put("cells", cells);
            json.put("capacity", capacity);
            json.put("cycles", cycles);
            json.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject formatAddCycleParameter(String serverID, int cycles) {
        JSONObject json = new JSONObject();


        try {
            json.put("id", serverID);
            json.put("cycles", cycles);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject formatUpdateParameters(Battery battery) {
        JSONObject json = new JSONObject();


        try {
            json.put("brand", battery.getBrand());
            json.put("model", battery.getModel());
            json.put("cells", battery.getNbS());
            json.put("capacity", battery.getCapacity());
            json.put("name", battery.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public class Routes

    {
        public static final String GET_BATTERIES="batteries";
        public static final String UPDATE_BATTERY="batteries";
        public static final String SIGNUP="auth/signup";
        public static final String SIGNOUT = "auth/signout";
        public static final String SIGNIN = "auth/signin";
        public static final String NEW_BATTERY = "batteries";
        public static final String ADD_CYCLE = "batteries";

    }
}

