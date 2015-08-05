package com.theboredengineers.easylipo.network.listeners;

/**
 * Created by Alex on 16/06/2015.
 */
public interface NetworkCommandListener {

    void onNetworkTaskEnd(Boolean success, Object json);
}
