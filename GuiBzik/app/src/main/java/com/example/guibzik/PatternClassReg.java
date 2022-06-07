package com.example.guibzik;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternClassReg {

    public boolean checkEmailPattern(String email){
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if(email.isEmpty()){
            return false;
        }
        return matcher.matches();
    }

    public boolean checkPasswordPattern(String password){
        String regex = ".{6,}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
