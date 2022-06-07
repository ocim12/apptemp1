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

import com.example.accr.AuxClasses.PagerAdapterAdmin;
import com.example.accr.AuxClasses.PagerAdapterUser;
import com.example.accr.AuxClasses.SharedPreferencesManager;
import com.example.accr.Dtos.UserLoginResponse;
import com.example.accr.R;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

public class LoggedAdminActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView welcomeText;
    private ImageButton logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_admin);
        initializeWidgets();
    }

    private void initializeWidgets() {
        Intent intent = getIntent();
        String userInfo = intent.getExtras().getString("user_info");
        Gson gson = new Gson();
        UserLoginResponse userLoginResponse = gson.fromJson(userInfo, UserLoginResponse.class);


        welcomeText = findViewById(R.id.welcome_admin);
        logoutButton = findViewById(R.id.logout_admin);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        tabLayout = findViewById(R.id.tab_layout_admin);
        viewPager = findViewById(R.id.view_pager_admin);

        welcomeText.setText("Witaj, " + userLoginResponse.userName);

        SharedPreferencesManager.addToken(userLoginResponse.token);
        SharedPreferencesManager.addUser(userLoginResponse);

        PagerAdapterAdmin pagerAdapter = new PagerAdapterAdmin(getSupportFragmentManager(), tabLayout.getTabCount(), userLoginResponse);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(LoggedAdminActivity.this);
        builder.setMessage("Wylogować się?").setPositiveButton("TAK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(LoggedAdminActivity.this, LoggingActivity.class);
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