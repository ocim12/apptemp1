package com.example.accr.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.accr.AuxClasses.Animation;
import com.example.accr.Dtos.UserRegisterRequest;
import com.example.accr.R;
import com.example.accr.ApiConnection.RegisterAsyncHttpClient;

public class RegisterActivity extends AppCompatActivity {

    private LinearLayout registerField;

    private EditText loginRegister;
    private EditText nameRegister;
    private EditText surnameRegister;
    private EditText emailRegister;
    private EditText phoneRegister;
    private EditText passwordRegister;
    private EditText passwordConfirmationRegister;
    private Switch isAdmin;
    private Button regi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initializeView();
    }

    private void initializeView() {
        registerField = findViewById(R.id.register_field);

        loginRegister = findViewById(R.id.login_register);
        nameRegister = findViewById(R.id.name_register);
        surnameRegister = findViewById(R.id.surname_register);
        emailRegister = findViewById(R.id.email_register);
        phoneRegister = findViewById(R.id.phone_register);
        passwordRegister = findViewById(R.id.password_register);
        passwordConfirmationRegister = findViewById(R.id.password_confirmation_register);
        isAdmin = findViewById(R.id.switch_is_admin);
        regi = findViewById(R.id.regi);
        regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        Animation animation = new Animation();
        animation.showElement(registerField, 1000);
    }

    private void register() {
        String result = "";
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.userName = loginRegister.getText().toString();
        userRegisterRequest.name = nameRegister.getText().toString();
        userRegisterRequest.surname = surnameRegister.getText().toString();
        userRegisterRequest.email = emailRegister.getText().toString();
        userRegisterRequest.phoneNumber = phoneRegister.getText().toString();
        userRegisterRequest.password = passwordRegister.getText().toString();
        if (isAdmin.isChecked()) {
            userRegisterRequest.role = "Admin";
        } else {
            userRegisterRequest.role = "User";
        }
        try {
            result = new RegisterAsyncHttpClient().execute(userRegisterRequest).get();
            Intent i = new Intent(this, LoggingActivity.class);
            i.putExtra("user_info", result);
            startActivity(i);
            finish();
        } catch (Exception e) {
            result = e.getMessage();
        } finally {
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        }
    }


}