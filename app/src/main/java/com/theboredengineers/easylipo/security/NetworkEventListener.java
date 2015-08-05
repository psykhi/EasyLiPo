package com.theboredengineers.easylipo.security;

import android.content.Context;

/**
 * Created by Alex on 14/06/2015.
 */
public interface NetworkEventListener {
    void onLoginSuccess(Context context);

    void onLoginFail(String error);

    void onEvent(int event);
}
