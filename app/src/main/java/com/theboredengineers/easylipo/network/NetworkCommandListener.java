package com.theboredengineers.easylipo.network;

/**
 * Created by Alex on 16/06/2015.
 */
public interface NetworkCommandListener {

    public void onNetworkTaskEnd(Boolean success, Object json);
}
