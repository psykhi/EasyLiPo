package com.theboredengineers.easylipo.network.command;

import com.theboredengineers.easylipo.network.NetworkCommand;
import com.theboredengineers.easylipo.network.NetworkCommandListener;
import com.theboredengineers.easylipo.network.server.RemoteServer;

import org.json.JSONObject;

/**
 * Created by Alex on 25/07/2015.
 */
public class SignupCommand extends NetworkCommand {

    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;

    public SignupCommand(String firstName,
                         String lastName,
                         String email,
                         String username,
                         String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
    }


    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public String getRoute() {
        return RemoteServer.Routes.SIGNUP;
    }

    @Override
    public String getName() {
        return "Sign Up";
    }

    @Override
    public JSONObject getJSON() {
        return RemoteServer.formatSignupParameters(username,password,firstName,lastName,email);
    }
}
