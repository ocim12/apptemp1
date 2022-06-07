package com.example.guibzik;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import java.util.function.ToDoubleBiFunction;

public class LoggingProblem extends Activity {

    ImageView imageViewSun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.ProblemTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logging_problem);
        initControls();
        startAnimation();
    }

    private void initControls() {
        imageViewSun = findViewById(R.id.imageViewSun);
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

    public void forgottenPassword(View view) {
        Intent intent = new Intent(LoggingProblem.this, RecoveringPass.class);
        startActivity(intent);
    }

    public void newAccount(View view) {
        Intent intent = new Intent(LoggingProblem.this,RegisterActivity.class);
        startActivity(intent);
    }

    public void aboutAuthors(View view) {
        //TODO
    }
}
