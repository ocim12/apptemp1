package com.example.accr.AuxClasses;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.accr.Dtos.UserLoginResponse;
import com.example.accr.Fragments.AccrPatternsAdminFragment;
import com.example.accr.Fragments.AccrsAdminFragment;
import com.example.accr.Fragments.MenageUsersAdminFragment;

public class PagerAdapterAdmin extends FragmentPagerAdapter {

    private int numOfTabs;
    private int selected;
    private UserLoginResponse userLoginResponse;

    public PagerAdapterAdmin(FragmentManager fm, int numOfTabs, UserLoginResponse userLoginResponse) {
        super(fm,numOfTabs);
        this.numOfTabs = numOfTabs;
        this.userLoginResponse = userLoginResponse;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                selected = 0;
                return new MenageUsersAdminFragment(userLoginResponse);
            case 1:
                selected = 1;
                return new AccrPatternsAdminFragment(userLoginResponse);
            case 2:
                selected = 2;
                return new AccrsAdminFragment(userLoginResponse);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
