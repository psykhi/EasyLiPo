package com.theboredengineers.easylipo.network;

import android.os.AsyncTask;
import android.util.Log;

import com.theboredengineers.easylipo.security.NetworkEventListener;
import com.theboredengineers.easylipo.security.AuthManager;

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
public class LoginTask extends AsyncTask<Object,Integer,Boolean> {
    private ArrayList<NetworkEventListener> listeners = null;
    @Override
    protected Boolean doInBackground(Object... params) {
        listeners = (ArrayList<NetworkEventListener>) params[2];
//        String resp = executePost(RemoteServer.getUrl() + "auth/signin", RemoteServer.formatSigninParameters((String) params[0], (String) params[1]));
//        if (resp == null)
//            return false;
//        else
//        {
//            Log.d("response",resp);
//            return true;
//        }
        return true;
    }


    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(aBoolean)
        {
            if(listeners != null)
            {
                for (int i = 0; i < listeners.size(); i++) {
                  //  listeners.get(i).onLoginSuccess(context);
                }
            }
        }
        else
        {
            if(listeners != null)
            {
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).onLoginFail();
                }
            }
        }
    }

    public static String executePost(String targetURL, String urlParameters)
    {
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/json");

           // connection.setRequestProperty("Cookie",
             //       "connect.sid="+ SessionManager.getInstance().getNewSessionId());

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream ());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            Log.d("lol", connection.getResponseCode()+" lo");
            Log.d("lol",connection.getResponseMessage());

            if(connection.getResponseCode() == 200)
            {
                String headerName=null;
                for (int i=1; (headerName = connection.getHeaderFieldKey(i))!=null; i++) {
                    String prop = connection.getHeaderFieldKey(i);
                    if (headerName.equals("set-cookie")) {
                        String cookie = connection.getHeaderField(i);
                        if (cookie.startsWith("connect.sid"))
                            AuthManager.getInstance().setSessionId(cookie);
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
                return response.toString();
            }



        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }
}
