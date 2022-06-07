package com.example.guibzik;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LoggedUser extends Activity implements AdapterView.OnItemSelectedListener {

    FirebaseAuth mAuth;
    private int counter = 0;
    private boolean flag1 = false;
    ImageView dropOfWater;
    RelativeLayout popUp,mainRelative;
    TextView powitanieTextView,currentDate,dailyHydrate;
    Button beHydrateButton;
    Spinner leftSpinner;
    int dailySummary = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.mainMenuTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_user);
        mAuth = FirebaseAuth.getInstance();
        initControls();
    }

    @Override
    public void onBackPressed() {
        if (flag1) {
            Intent intent = new Intent(LoggedUser.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        flag1 = true;
        Toast.makeText(this, "Kliknij raz jeszcze aby się wylogować.", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                flag1 = false;
            }
        }, 2000);
    }

    private void initControls() {
        dropOfWater = findViewById(R.id.dropOfWater);
        popUp = findViewById(R.id.popUpInfo);
        popUp.setVisibility(View.INVISIBLE);
        powitanieTextView = findViewById(R.id.textViewPowitanie);
        String displayNameUser = mAuth.getCurrentUser().getDisplayName();
        powitanieTextView.setText(displayNameUser);
        mainRelative = findViewById(R.id.mainRelative);
        popUp.setClickable(false);
        currentDate = findViewById(R.id.currentDate);
        String date = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        currentDate.setText(date);
        beHydrateButton = findViewById(R.id.beHydrateButton);
        dailyHydrate = findViewById(R.id.dailyHydrate);
        leftSpinner = findViewById(R.id.leftSpinner);

        List<String> list = new ArrayList<String>();


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);



    }

    public void userInformation(View view) {
        startAnimation();
    }

    public void startAnimation() {
        AnimationSet animationSet = new AnimationSet(true);

        final Animation animationDown = new TranslateAnimation(0f, 0f, 0f, 250f);
        animationDown.setDuration(350);

        Animation animationScale = new ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animationScale.setDuration(350);

        final Animation animationPopUp = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animationPopUp.setDuration(350);

        animationSet.addAnimation(animationScale);
        animationSet.addAnimation(animationDown);

        dropOfWater.startAnimation(animationSet);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                popUp.startAnimation(animationPopUp);
                dropOfWater.setVisibility(View.GONE);
                popUp.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void closePopUp(View view) {
        final Animation animationPopUp = new ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animationPopUp.setDuration(350);
        popUp.startAnimation(animationPopUp);

        final Animation animationDropOfWater = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animationDropOfWater.setDuration(350);


        animationPopUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                popUp.setVisibility(View.GONE);
                dropOfWater.startAnimation(animationDropOfWater);
                dropOfWater.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void beHydrateAction(View view) {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Potwierdź aktywność").setMessage("Dodać aktywność?").setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dailySummary = Integer.parseInt(leftSpinner.getSelectedItem().toString()) + dailySummary;
                String waterDaily = "Dzisiaj wypiłeś " + dailySummary + "ml wody.";
                dailyHydrate.setText(waterDaily);
            }
        }).setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
