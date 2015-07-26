package com.theboredengineers.easylipo.network;

import android.os.AsyncTask;
import android.util.Log;

import com.theboredengineers.easylipo.network.server.RemoteServer;
import com.theboredengineers.easylipo.security.AuthManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

/**
 * Created by Alex on 14/06/2015.
 */
public class NetworkSyncTask extends AsyncTask<Object,Integer,Boolean>{

    private ExecutorService pool;
    private ArrayList<NetworkSyncListener> listeners;
    private ArrayList<NetworkTask> commands;
    @Override
    protected Boolean doInBackground(Object... params) {
        commands = (ArrayList<NetworkTask>)params[0];
        listeners = (ArrayList<NetworkSyncListener>) params[1];
        pool = (ExecutorService) params[2];

        Log.d("DEBUG","Commands left :"+commands.size());

        refresh();

        return true;
    }




    private void refresh()
    {
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(RemoteServer.getUrl()+RemoteServer.Routes.GET_BATTERIES);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept",
                    "application/json");

            connection.setRequestProperty("Cookie",
                   AuthManager.getInstance().getSessionId());

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(false);

            Log.d("lol", connection.getResponseCode()+" lo");
            Log.d("lol",connection.getResponseMessage());

            if(connection.getResponseCode() == 200)
            {
                String headerName=null;

                //Get Response
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                Log.d("get batteries", response.toString());
            }



        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
        Log.d("network", "Refreshing");
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        Log.d("SYNC","End of Sync");
        if(listeners != null) {
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).onNetworkSyncEnded(true);
            }
        }
    }
}
