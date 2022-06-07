package com.example.accr.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.accr.Dtos.UserLoginResponse;
import com.example.accr.AuxClasses.PagerAdapterUser;
import com.example.accr.R;
import com.example.accr.AuxClasses.SharedPreferencesManager;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

public class LoggedUserActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView welcomeText;
    private ImageButton logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_user);
        initializeView();
    }

    private void initializeView() {
        Intent intent = getIntent();
        String userInfo = intent.getExtras().getString("user_info");
        Gson gson = new Gson();
        UserLoginResponse userLoginResponse = gson.fromJson(userInfo, UserLoginResponse.class);
        welcomeText = findViewById(R.id.welcome);
        logoutButton = findViewById(R.id.logout);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        welcomeText.setText("Witaj, " + userLoginResponse.userName);

        SharedPreferencesManager.addToken(userLoginResponse.token);
        SharedPreferencesManager.addUser(userLoginResponse);

        PagerAdapterUser pagerAdapter = new PagerAdapterUser(getSupportFragmentManager(), tabLayout.getTabCount(), userLoginResponse);
        viewPager.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                TabLayout.Tab tab = tabLayout.getTabAt(position);
                tab.select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }



    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoggedUserActivity.this);
        builder.setMessage("Wylogować się?").setPositiveButton("TAK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(LoggedUserActivity.this, LoggingActivity.class);
                startActivity(i);
                finish();
            }
        }).setNegativeButton("NIE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }
}