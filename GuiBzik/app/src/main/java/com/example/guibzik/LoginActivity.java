package com.example.guibzik;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends Activity {

    FirebaseAuth mAuth;
    ImageView backBut;
    TextView registerView, problemLogging;
    EditText loginEditText, passwordEditText;
    Button loginButton;
    ProgressBar progressBar2;
    PatternClassReg patternClassReg = new PatternClassReg();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initControls();
        mAuth = FirebaseAuth.getInstance();
    }

    private void initControls() {
        backBut = findViewById(R.id.backBtn);
        backBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        registerView = findViewById(R.id.registerTextViewLoginActi);
        registerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginEditText = findViewById(R.id.loginTextField);
        passwordEditText = findViewById(R.id.passwordTextField);
        loginButton = findViewById(R.id.loginButtonLogAct);
        progressBar2 = findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.GONE);
        problemLogging = findViewById(R.id.problemLogging);

    }

    public void logIn(View view) {
        String email = loginEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
//        String email = "bartosz.ocimek@gmail.com";
//        String password = "123456";
      //  Toast.makeText(this, email + " haslo " + password, Toast.LENGTH_SHORT).show();

        if (!patternClassReg.checkPasswordPattern(password)) {
            Toast.makeText(this, "Hasło jest za krótkie.", Toast.LENGTH_SHORT).show();
        }
        if (!patternClassReg.checkEmailPattern(email)) {
            Toast.makeText(this, "Niepoprawny adres e-mail", Toast.LENGTH_SHORT).show();
        } else {
            signIn(email, password);
        }
    }

    public void signIn(String email, String password) {
        progressBar2.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar2.setVisibility(View.GONE);
                            Intent intent = new Intent(LoginActivity.this, LoggedUser.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Wprowadzony e-mail bądź hasło jest nieprawidłowe.", Toast.LENGTH_SHORT).show();
                            progressBar2.setVisibility(View.GONE);
                        }
                    }
                });
    }

    public void problemWithLogging(View view) {
        Intent intent = new Intent(LoginActivity.this, LoggingProblem.class);
        startActivity(intent);
    }
}
