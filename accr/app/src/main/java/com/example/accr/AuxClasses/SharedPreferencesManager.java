package com.example.accr.AuxClasses;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.accr.Dtos.UserLoginResponse;
import com.google.gson.Gson;

import org.json.JSONObject;

public class SharedPreferencesManager {

    private static SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
    private static SharedPreferences.Editor myEditor;

    public static void addToken(String token) {
        myEditor = myPreferences.edit();
        myEditor.putString("token", token);
        myEditor.apply();
    }

    public static void addUser(UserLoginResponse userLoginResponse) {
        myEditor = myPreferences.edit();
        String jsonObj = new Gson().toJson(userLoginResponse);
        myEditor.putString("user", jsonObj);
        myEditor.apply();
    }

    public static String getToken() {
        return myPreferences.getString("token", "");
    }

    public static UserLoginResponse getUser() {
        String user = myPreferences.getString("user", "");
        UserLoginResponse resp = new Gson().fromJson(user, UserLoginResponse.class);

        return resp;
    }

}
