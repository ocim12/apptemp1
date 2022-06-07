package com.example.guibzik;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends Activity {

    private FirebaseAuth mAuth;
    Button registerButton;
    Button loginButton;
    EditText emailEditText;
    EditText passwordEditText1,passwordEditText2;
    ProgressBar progressBarRegister;
    PatternClassReg patternClassReg = new PatternClassReg();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.RegisterTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initControls();
        mAuth = FirebaseAuth.getInstance();
    }

    private void initControls() {
        emailEditText = findViewById(R.id.emailTextField);
        passwordEditText1 = findViewById(R.id.passwordTextField1);
        registerButton = findViewById(R.id.registerButton);
        progressBarRegister = findViewById(R.id.progressBarRegister);
        progressBarRegister.setVisibility(View.GONE);
        passwordEditText2 = findViewById(R.id.passwordTextField2);
    }

    public void registerUser(View view) {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText1.getText().toString().trim();
        if(!passwordEditText1.getText().toString().isEmpty() ||!passwordEditText2.getText().toString().isEmpty() || !emailEditText.getText().toString().isEmpty()){
            if(password.length() < 6){
                Toast.makeText(this, "Hasło jest za krótkie!", Toast.LENGTH_SHORT).show();
            }
            if(!password.equals(passwordEditText2.getText().toString()) || password.length() > 5){
                Toast.makeText(this, "Hasła nie pasują do siebie!", Toast.LENGTH_SHORT).show();
            }
            if(!patternClassReg.checkEmailPattern(email)){
                Toast.makeText(this, "Nieprawidłowy adres e-mail!", Toast.LENGTH_SHORT).show();
            }
            else{
                createAccount(email, password);
            }
        }
    }

    private void createAccount(String email, String password) {
        progressBarRegister.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBarRegister.setVisibility(View.GONE);
                            Intent intent = new Intent(RegisterActivity.this, SettingInfoActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void logIn(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
