package com.theboredengineers.easylipo.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.theboredengineers.easylipo.network.server.RemoteServer;
import com.theboredengineers.easylipo.security.AuthManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Alex on 14/06/2015.
 */
public class NetworkTask extends AsyncTask<Object,Long,Integer>{

    private NetworkCommand command;
    private Object json = null;
    private String method = null;
    private ArrayList<NetworkCommand> commands;

    @Override
    protected Integer doInBackground(Object... params) {
        URL url;
        HttpURLConnection connection = null;
        Context context = (Context) params[0];
        command = (NetworkCommand) params[1];
        method = command.getMethod();
        Integer ret = 404;
        try {
            //Create connection
            url = new URL(RemoteServer.getUrl()+command.getRoute());
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod(method);
            if(method.equals("GET"))
                connection.setRequestProperty("Accept",
                        "application/json");
            else
                connection.setRequestProperty("Content-Type",
                        "application/json");

            connection.setRequestProperty("Cookie",
                    AuthManager.getInstance().getSessionId(context));

            connection.setUseCaches(false);
            connection.setDoInput(true);

            if (method.equals("GET") || method.equals("DELETE"))
                connection.setDoOutput(false);
            else {
                connection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(
                        connection.getOutputStream ());
                wr.writeBytes(command.getJSON().toString());
                wr.flush();
                wr.close();
            }

//            Log.d("lol",connection.getResponseMessage());

            ret = connection.getResponseCode();
            if(ret == 200)
            {
                // Cookie management
                String headerName=null;
                for (int i=1; (headerName = connection.getHeaderFieldKey(i))!=null; i++) {
                    String prop = connection.getHeaderFieldKey(i);
                    if (headerName.equals("set-cookie")) {
                        String cookie = connection.getHeaderField(i);
                        if (cookie.startsWith("connect.sid"))

                        command.onSessionIDCookie(cookie);
                    }
                }
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
                Log.d("Network command " + command.getName() + " response :", response.toString());

                json =  new JSONTokener(response.toString()).nextValue();

                }
            else
            {
                //Get Response
                InputStream is = connection.getErrorStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                Log.d("Network command "+command.getName(), response.toString());

                json =  new JSONTokener(response.toString()).nextValue();
            }


        } catch (Exception e) {
            json = new JSONObject();
            try {
                ((JSONObject) json).put("message", "Couldn't not connect to the server");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            ret = 404;

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }

        return ret;
    }

    @Override
    protected void onPostExecute(Integer code) {
        Boolean success = false;
        super.onPostExecute(code);
        if(code == 200)
        {
            success = true;
        }
        if(command.getListener() != null)
        {
            command.getListener().onNetworkTaskEnd(success,json);
        }

    }
}
