package com.theboredengineers.easylipo.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.theboredengineers.easylipo.model.BatteryManager;
import com.theboredengineers.easylipo.network.NetworkManager;
import com.theboredengineers.easylipo.network.listeners.NetworkCommandListener;
import com.theboredengineers.easylipo.network.server.RemoteServer;

import java.util.ArrayList;

import static com.theboredengineers.easylipo.network.NetworkEvent.SIGNOUT_FAIL;
import static com.theboredengineers.easylipo.network.NetworkEvent.SIGNOUT_SUCESS;

/**
 * Created by Alex on 14/06/2015.
 */
public class AuthManager implements NetworkEventListener {
    private static AuthManager instance = null;
    private boolean loggedIn = false;
    private ArrayList<NetworkEventListener> listeners = null;
    private String sessionId = null;

    private AuthManager() {
        listeners = new ArrayList<NetworkEventListener>();
    }


    public static AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }


    public boolean isLoggedIn(Context context) {
        if (!getSessionId(context).equals("lol"))
            loggedIn = true;
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void attemptLogin(String username, String pwd, final NetworkEventListener listener, final Context context) {
        listeners.add(listener);
        listeners.add(this);
        NetworkManager.getInstance().attemptLogin(context, username, pwd, new NetworkCommandListener() {
            @Override
            public void onNetworkTaskEnd(Boolean success, Object json) {
                if (success) {
                    loggedIn = true;

                    listener.onLoginSuccess(context);
                } else {
                    String errorMessageFromJSON = RemoteServer.getErrorMessageFromJSON(json);
                    listener.onLoginFail(errorMessageFromJSON);
                }
            }
        });


    }

    public void signOut(final Context context, final NetworkEventListener l)
    {
        BatteryManager.getInstance(context).removeAllBatteries();
        NetworkManager.getInstance().signout(context, new NetworkCommandListener() {
            @Override
            public void onNetworkTaskEnd(Boolean success, Object json) {
                if (success) {
                    loggedIn = false;
                    context.getSharedPreferences("easylipo", Context.MODE_PRIVATE).edit().remove("session").commit();
                    getSessionId(context);
                    l.onEvent(SIGNOUT_SUCESS);
                } else {
                    l.onEvent(SIGNOUT_FAIL);
                }

            }
        });

    }

    @Override
    public void onLoginSuccess(Context context) {
        setLoggedIn(true);
        NetworkManager.getInstance().networkSync(context,null);
    }

    @Override
    public void onLoginFail(String error) {

    }

    @Override
    public void onEvent(int event) {

    }


    public String getSessionId(Context context) {

        sessionId = context.getSharedPreferences("easylipo", Context.MODE_PRIVATE).getString("session","lol");
        //Log.d("get session",sessionId);
        return sessionId;
    }

    public void setSessionId(Context context, String cookie) {
        SharedPreferences preferences = context.getSharedPreferences("easylipo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =preferences.edit();
        editor.putString("session", cookie).commit();

        Log.d("session", context.getSharedPreferences("easylipo", Context.MODE_PRIVATE).getString("session", "lol"));
        this.sessionId = cookie;
    }

}