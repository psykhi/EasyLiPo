package com.theboredengineers.easylipo.network.server;


import com.theboredengineers.easylipo.objects.Battery;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Alex on 14/06/2015.
 */
public  class RemoteServer{
    private static final String url="http://easylipo.theboredengineers.com";

    public static String getUrl()
    {
        return url+"/";
    }

    public static JSONObject formatNewBatteryParameters(String brand, String model, int cells,
                                                        int capacity, int cycles,
                                                        int dischargeRate, int chargeRate,
                                                        Date purchaseDate, boolean charged, String name) {
        JSONObject json = new JSONObject();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);


        try {
            json.put("brand", brand);
            json.put("bought", format.format(purchaseDate));
            json.put("charged", charged);
            json.put("brand", brand);
            json.put("discharge", dischargeRate);
            json.put("charge", chargeRate);
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

    public static JSONObject formatUpdateParameters(Battery battery) {
        JSONObject json = new JSONObject();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);

        try {
            json.put("brand", battery.getBrand());
            json.put("bought", format.format(battery.getPurchaseDate()));
            json.put("charged", battery.isCharged());
            json.put("brand", battery.getBrand());
            json.put("discharge", battery.getDischargeRate());
            json.put("charge", battery.getChargeRate());
            json.put("model", battery.getModel());
            json.put("cells", battery.getNbS());
            json.put("capacity", battery.getCapacity());
            json.put("name", battery.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static Battery getBatteryFromJSON(JSONObject jsonObject) {
        Battery batt = new Battery();
        String name = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            Date date = format.parse(jsonObject.getString("bought"));
            name = jsonObject.getString("name");

            batt.setName(name);
            batt.setPurchaseDate(date);
            batt.setServer_id(jsonObject.getString("_id"));
            batt.setNbOfCycles(jsonObject.getInt("cycles"));
            batt.setChargeRate(jsonObject.getInt("charge"));
            batt.setDischargeRate(jsonObject.getInt("discharge"));
            batt.setCharged(jsonObject.getBoolean("charged"));
            batt.setBrand(jsonObject.getString("brand"));
            batt.setModel(jsonObject.getString("model"));
            batt.setNbS(jsonObject.getInt("cells"));
            batt.setCapacity(jsonObject.getInt("capacity"));

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
            return null;
        }
        return batt;
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



    public static String getErrorMessageFromJSON(Object json) {
        try {
            return ((JSONObject)json).getString("message");
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception reading response from server";
        }
    }

    public static Object formatErrorMessageJSON(String error) {
        JSONObject json = new JSONObject();
        try {
            json.put("message", error);
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
        public static final String DELETE_BATTERY = "batteries";

    }
}

