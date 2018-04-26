package com.example.ivanovnv.myfirstapplication;

import android.content.Context;
import android.content.SharedPreferences;


import com.example.ivanovnv.myfirstapplication.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;

import java.util.List;


/**
 * Created by IvanovNV on 21.02.2018.
 */

public class SharedPreferencesHelper {
    public static final String SHARED_PREF_NAME = "SHARED_PREF_NAME";
    public static final String USERS_KEY = "USERS_KEY";
    public static final Type USRS_TYPE = new TypeToken <List<User>>(){}.getType();

    private SharedPreferences mSharedPreferences;
    private Gson mGson = new Gson();


    public SharedPreferencesHelper(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
    }

    public List<User> getUsers(){
        List<User> users = mGson.fromJson(mSharedPreferences.getString(USERS_KEY,""),USRS_TYPE);
        return users == null ? new ArrayList<User>():users;
    }

    public boolean addUser(User user){
        List<User> users = getUsers();
        for(User u:users) {
            if (u.getEmail().equalsIgnoreCase(user.getEmail())) {
                return false;
            }
        }

        users.add(user);
        mSharedPreferences.edit().putString(USERS_KEY,mGson.toJson(users,USRS_TYPE)).apply();
        return true;
    }

    public User login(String login,String password){
        List<User> users = getUsers();
        for(User u : users) {
            if(login.equalsIgnoreCase(u.getEmail())
                    && password.equals(u.getPassword())) {
                u.setHasSuccessLogin(true);
                mSharedPreferences.edit().putString(USERS_KEY,mGson.toJson(users,USRS_TYPE)).apply();
                return u;
            }
        }
        return null;
    }

   public List<String> getSuccessLogins() {
        List<String> successLogins = new ArrayList<>();
        List<User> allUsers = getUsers();

        for(User u : allUsers) {
            if(u.getHasSuccessLogin()) {
                successLogins.add(u.getEmail());
            }
        }

    return successLogins;
   }
}
