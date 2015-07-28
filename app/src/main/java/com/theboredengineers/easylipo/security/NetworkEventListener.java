package com.theboredengineers.easylipo.security;

import android.content.Context;

import com.theboredengineers.easylipo.network.NetworkEvent;

/**
 * Created by Alex on 14/06/2015.
 */
public interface NetworkEventListener {
    public void onLoginSuccess(Context context);
    public void onLoginFail(String error);

    public void onEvent(int event);
}
