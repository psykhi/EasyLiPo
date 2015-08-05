package com.theboredengineers.easylipo.network.listeners;

/**
 * Created by Alex on 14/06/2015.
 */
public interface NetworkSyncListener {

    void onNetworkSyncEnded(Boolean success, String errorMessageFromJSON);

}
