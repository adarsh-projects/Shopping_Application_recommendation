package com.example.trima.prevalent;

import android.widget.Toast;

import com.example.trima.model.User;

public class prevalent {
    public static User CurrentOnlineUser;
    public static final String userPhoneKey = "UserPhone";
    public static final String userPassword  = "UserPassword";
    public prevalent(User CurrentOnlineUser){
        this.CurrentOnlineUser = CurrentOnlineUser;
    }
}