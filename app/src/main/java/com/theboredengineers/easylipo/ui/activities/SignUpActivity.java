package com.theboredengineers.easylipo.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.theboredengineers.easylipo.R;
import com.theboredengineers.easylipo.network.NetworkManager;
import com.theboredengineers.easylipo.network.listeners.NetworkCommandListener;

import org.json.JSONObject;

/**
 * Created by Alex on 14/06/2015.
 */
public class SignUpActivity extends BaseActivity implements NetworkCommandListener{
    private Button loginButton;
    private EditText usernameEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private TextView loginTextView;
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
        setContentView(R.layout.activity_signup);
    }

    @Override
    protected void bindViews() {
        super.bindViews();
        loginButton = (Button) findViewById(R.id.buttonn_signup);
        usernameEditText = (EditText) findViewById(R.id.signup_username);
        firstNameEditText = (EditText) findViewById(R.id.signup_first_name);
        lastNameEditText = (EditText) findViewById(R.id.signup_last_name);
        emailEditText = (EditText) findViewById(R.id.signup_email);
        passwordEditText = (EditText) findViewById(R.id.signup_password);
        loginTextView = (TextView) findViewById(R.id.textview_login);
    }

    @Override
    protected void setListeners() {
        super.setListeners();
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
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE)
                    createAccount();
                return false;
            }
        });
    }

    private void createAccount() {
        progressDialog.setMessage("Signing up...");
        progressDialog.show();
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
        progressDialog.dismiss();
        if(success)
        {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            JSONObject error;
            try {
                error = (JSONObject) json;
                Toast.makeText(this,error.getString("message"),Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this,getString(R.string.unexpected_error),Toast.LENGTH_SHORT).show();
            }
        }
    }
}

