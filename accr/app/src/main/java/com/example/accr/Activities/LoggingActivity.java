package com.example.accr.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.accr.AuxClasses.Animation;
import com.example.accr.Dtos.UserLoginRequest;
import com.example.accr.Dtos.UserLoginResponse;
import com.example.accr.ApiConnection.LoginAsyncHttpClient;
import com.example.accr.R;
import com.google.gson.Gson;

public class LoggingActivity extends AppCompatActivity {

    private LinearLayout loginField;
    private EditText login;
    private EditText password;
    private TextView registerButton;

    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logging);
        initializeWidgets();
        Animation animation = new Animation();
        animation.showElement(loginField, 1000);
    }

    private void initializeWidgets() {
        loginField = findViewById(R.id.login_field);
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> {
            loginButtonOnClick();
        });
        loginField.setVisibility(View.INVISIBLE);
        registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void loginButtonOnClick() {
        String result = "";
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.userName = login.getText().toString();
        userLoginRequest.password = password.getText().toString();
        try {
            result = new LoginAsyncHttpClient().execute(userLoginRequest).get();
            Gson gson = new Gson();
            UserLoginResponse userLoginResponse = gson.fromJson(result, UserLoginResponse.class);
            if (userLoginResponse.role.equals("Admin")) {
                Intent i = new Intent(this, LoggedAdminActivity.class);
                i.putExtra("user_info", result);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(this, LoggedUserActivity.class);
                i.putExtra("user_info", result);
                startActivity(i);
                finish();
            }


        } catch (Exception e) {
            result = e.getMessage();
        } finally {
            //   Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        }
    }

    private void register() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}