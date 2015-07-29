package com.theboredengineers.easylipo.network;

/**
 * Created by Alex on 14/06/2015.
 */
public interface NetworkSyncListener {

    public void onNetworkSyncEnded(Boolean success, String errorMessageFromJSON);

}
