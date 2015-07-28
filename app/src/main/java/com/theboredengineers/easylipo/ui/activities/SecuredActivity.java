package com.theboredengineers.easylipo.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.theboredengineers.easylipo.network.NetworkEvent;
import com.theboredengineers.easylipo.security.AuthManager;
import com.theboredengineers.easylipo.security.NetworkEventListener;

/**
 * Created by Alex on 14/06/2015.
 */
public class SecuredActivity extends BaseActivity {

    private ProgressDialog signoutDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signoutDialog = new ProgressDialog(this);
        AuthManager.getInstance().setContext(this);
        if (!AuthManager.getInstance().isLoggedIn()) {
            redirectToLogin();
            Log.d("Secured activity","Redirecting to login");
        }
        else
        {
            Log.d("Secured activity","Logged in");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void redirectToLogin() {
        finish();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    protected void signOut()
    {
        signoutDialog.setMessage("Signing out...");
        signoutDialog.show();
        AuthManager.getInstance().signOut(new NetworkEventListener() {
            @Override
            public void onLoginSuccess(Context context) {

            }

            @Override
            public void onLoginFail(String error) {

            }

            @Override
            public void onEvent(int event) {
                if (event == NetworkEvent.SIGNOUT_SUCESS) {
                    signoutDialog.dismiss();
                    redirectToLogin();
                }

            }
        });
    }
}
