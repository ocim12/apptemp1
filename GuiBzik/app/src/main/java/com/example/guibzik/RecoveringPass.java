package com.example.guibzik;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class RecoveringPass extends Activity {
    FirebaseAuth mAuth;

    ImageView imageViewSun;
    EditText emailEditT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.ProblemTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovering_pass);
        mAuth = FirebaseAuth.getInstance();
        initControls();
        startAnimation();
    }

    private void initControls(){
        imageViewSun = findViewById(R.id.imageViewSun1);
        emailEditT = findViewById(R.id.emailTextF);
    }
    private void startAnimation() {
        final Animation animation1 = new ScaleAnimation(1f, 0.9f, 1f, 0.9f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        final Animation animation2 = new ScaleAnimation(0.9f, 1f, 0.9f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation1.setDuration(1000);
        imageViewSun.startAnimation(animation1);
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animation2.setDuration(1000);
                imageViewSun.startAnimation(animation2);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animation1.setDuration(1000);
                imageViewSun.startAnimation(animation1);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void recoverPassword(View view) {
        Toast.makeText(this, "Sprawdź swoją skrzynkę pocztową.", Toast.LENGTH_SHORT).show();
        mAuth.sendPasswordResetEmail(emailEditT.getText().toString().trim());
    }
}
