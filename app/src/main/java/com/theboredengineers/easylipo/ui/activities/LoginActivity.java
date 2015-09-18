package com.theboredengineers.easylipo.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.theboredengineers.easylipo.R;
import com.theboredengineers.easylipo.network.NetworkManager;
import com.theboredengineers.easylipo.network.listeners.NetworkSyncListener;
import com.theboredengineers.easylipo.security.AuthManager;
import com.theboredengineers.easylipo.security.NetworkEventListener;

public class LoginActivity extends BaseActivity implements NetworkEventListener {
    private static final String TAG = "Login Activity ";
    private Button loginButton;
    private TextView signupButton;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
    }

    @Override
    protected void setView() {
        super.setView();
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void setListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                attemptLogin();
            }
        });
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE)
                    attemptLogin();
                return false;
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignupActivity();
            }
        });
    }

    @Override
    protected void bindViews() {
        loginButton = (Button) findViewById(R.id.button_login);
        signupButton = (TextView) findViewById(R.id.textview_signup);
        usernameEditText = (EditText) findViewById(R.id.login_username);
        passwordEditText = (EditText) findViewById(R.id.login_password);
    }

    private void goToSignupActivity() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    private void attemptLogin() {
        loginButton.setEnabled(false);
        progressDialog.setMessage(getString(R.string.authenticating));
        progressDialog.show();
        AuthManager.getInstance().attemptLogin(usernameEditText.getText().toString()
                , passwordEditText.getText().toString(), this, this);

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
    public void onLoginSuccess(Context context) {
        progressDialog.setMessage("Syncing data...");
        loginButton.setEnabled(true);
        NetworkManager.getInstance().networkSync(this,new NetworkSyncListener() {
            @Override
            public void onNetworkSyncEnded(Boolean success, String errorMessageFromJSON) {
                progressDialog.dismiss();
                Intent intent = new Intent(LoginActivity.this, ActivityBatteryList.class);
                intent.putExtra("synced",true);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onLoginFail(String error) {
        progressDialog.hide();
        loginButton.setEnabled(true);
        Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEvent(int event) {

    }

}
