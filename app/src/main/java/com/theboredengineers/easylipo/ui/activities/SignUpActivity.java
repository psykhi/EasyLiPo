package com.theboredengineers.easylipo.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.theboredengineers.easylipo.R;
import com.theboredengineers.easylipo.network.NetworkCommandListener;
import com.theboredengineers.easylipo.network.NetworkManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alex on 14/06/2015.
 */
public class SignUpActivity extends BaseActivity implements NetworkCommandListener{
    Button loginButton;
    EditText usernameEditText;
    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText emailEditText;
    TextView loginTextView;

    EditText passwordEditText;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);

        setContentView(R.layout.activity_signup);
        loginButton = (Button) findViewById(R.id.buttonn_signup);
        usernameEditText = (EditText) findViewById(R.id.signup_username);
        firstNameEditText = (EditText) findViewById(R.id.signup_first_name);
        lastNameEditText = (EditText) findViewById(R.id.signup_last_name);
        emailEditText = (EditText) findViewById(R.id.signup_email);
        passwordEditText = (EditText) findViewById(R.id.signup_password);
        loginTextView = (TextView) findViewById(R.id.textview_login);

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        NetworkManager.getInstance().signup(
                firstNameEditText.getText().toString(),
                lastNameEditText.getText().toString(),
                usernameEditText.getText().toString(),
                emailEditText.getText().toString(),
                passwordEditText.getText().toString(),
                this,this
        );
    }



    @Override
    public void onNetworkTaskEnd(Boolean success, Object json)  {

        if(success)
        {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            JSONObject error;
            error = (JSONObject) json;

            try {
                Toast.makeText(this,error.getString("message"),Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                Toast.makeText(this,getString(R.string.unexpected_error),Toast.LENGTH_SHORT).show();
            }
        }
    }
}

