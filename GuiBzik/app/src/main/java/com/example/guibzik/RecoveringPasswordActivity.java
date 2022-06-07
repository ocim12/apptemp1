package com.example.guibzik;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class RecoveringPasswordActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    ImageView imageViewSun;
    EditText emailEditT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_recovering_password);

    }




}
