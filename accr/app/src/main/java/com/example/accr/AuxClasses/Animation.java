package com.example.accr.AuxClasses;

import android.content.Intent;
import android.view.View;
import android.view.animation.AlphaAnimation;

public class Animation {

    public void showHideElement(View view, int time) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(time);
        view.startAnimation(alphaAnimation);
        alphaAnimation.setFillAfter(true);

        alphaAnimation.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
            @Override
            public void onAnimationStart(android.view.animation.Animation animation) {

            }

            @Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                alphaAnimation.setDuration(time);
                view.startAnimation(alphaAnimation);
                alphaAnimation.setFillAfter(true);
            }

            @Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {

            }
        });
    }

    public void showElement(View view, int time) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(time);
        view.startAnimation(alphaAnimation);
        alphaAnimation.setFillAfter(true);
    }


}
