package com.theboredengineers.easylipo.network.command;

import com.theboredengineers.easylipo.network.NetworkCommand;
import com.theboredengineers.easylipo.network.server.RemoteServer;
import com.theboredengineers.easylipo.security.AuthManager;

import org.json.JSONObject;

/**
 * Created by Alex on 25/07/2015.
 */
public class SigninCommand extends NetworkCommand {
    private final String username;
    private final String pwd;

    public SigninCommand(String username, String pwd) {
        this.username = username;
        this.pwd = pwd;
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public String getRoute() {
        return RemoteServer.Routes.SIGNIN;
    }

    @Override
    public String getName() {
        return "Sign in";
    }

    @Override
    public JSONObject getJSON() {
        return RemoteServer.formatSigninParameters(username, pwd);
    }

    @Override
    public void onSessionIDCookie(String cookie) {
        AuthManager.getInstance().setSessionId(cookie);
    }
}
