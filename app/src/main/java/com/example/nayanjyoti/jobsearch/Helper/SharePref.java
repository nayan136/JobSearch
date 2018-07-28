package com.example.nayanjyoti.jobsearch.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharePref {

    private static SharePref sharePref = new SharePref();
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public static final String EMAIL = "email";
    public static final String NAME = "name";
    public static final String ROLE = "role";
    public static final String ID = "id";

    private SharePref() {} //prevent creating multiple instances by making the constructor private

    //The context passed into the getInstance should be application level context.
    public static SharePref getInstance(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
        return sharePref;
    }

    public void put(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String get(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    public void clearAll() {
        editor.clear();
        editor.commit();
    }
}
