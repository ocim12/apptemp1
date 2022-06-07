package com.example.guibzik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SettingInfoActivity extends Activity {

    ProgressBar progressBar;
    EditText nameEditText,surnameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.RegisterTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_info);
        initControls();
    }

    public void endRegistration(View view) {
        progressBar.setVisibility(View.VISIBLE);
        String nameLastName = nameEditText.getText().toString().trim() + " " + surnameEditText.getText().toString().trim();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(nameLastName).build();

        user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(SettingInfoActivity.this, LoggedUser.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void initControls(){
        progressBar = findViewById(R.id.progressBarRegisterName);
        nameEditText = findViewById(R.id.imieTextField);
        surnameEditText = findViewById(R.id.nazwiskoTextField);
        progressBar.setVisibility(View.GONE);
    }
}
