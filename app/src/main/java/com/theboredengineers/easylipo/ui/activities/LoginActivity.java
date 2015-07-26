package com.theboredengineers.easylipo.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.theboredengineers.easylipo.R;
import com.theboredengineers.easylipo.network.NetworkManager;
import com.theboredengineers.easylipo.network.NetworkSyncListener;
import com.theboredengineers.easylipo.security.NetworkEventListener;
import com.theboredengineers.easylipo.security.AuthManager;

public class LoginActivity extends BaseActivity implements NetworkEventListener {
    Button loginButton;
    Button signupButton;

    EditText usernameEditText;
    EditText passwordEditText;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);

        setContentView(R.layout.activity_login);
        loginButton = (Button) findViewById(R.id.buttonLogin);
        signupButton = (Button) findViewById(R.id.buttonLoginSignup);
        usernameEditText = (EditText) findViewById(R.id.login_username);
        passwordEditText = (EditText) findViewById(R.id.login_pwd);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignupActivity();
            }
        });
    }

    private void goToSignupActivity() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    private void attemptLogin() {
        AuthManager.getInstance().attemptLogin(usernameEditText.getText().toString()
                , passwordEditText.getText().toString(), this,this);
        progressDialog.setMessage("Signing in... ");
        progressDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("login task", "bye bye");
    }

    @Override
    public void onLoginSuccess(Context context) {
        progressDialog.setMessage("Syncing data...");
        NetworkManager.getInstance().networkSync(this,new NetworkSyncListener() {
            @Override
            public void onNetworkSyncEnded(Boolean success) {
                progressDialog.dismiss();
                //Toast.makeText(getApplicationContext(), getString(R.string.loginSuccess), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, ActivityBatteryList.class);
                intent.putExtra("synced",true);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onLoginFail() {
        progressDialog.hide();
        Toast.makeText(getApplicationContext(),getString(R.string.loginFail),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEvent(int event) {

    }

}
