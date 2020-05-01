package com.lubaarbel.pingarbel.utils;


import android.content.Context;
import android.content.SharedPreferences;

import com.lubaarbel.pingarbel.AppHolder;

/**
 * App Settings class used to persist user settings, like the need to bio-auth in our case
 * **/
public class SettingsPrefs {

    private static final String SHARED_PREFS_NAME = "appSettingsSharedPrefs";
    // app keys
    public static final String SHARED_PREFS_VALUE_SHOULD_BIO_AUTHENTICATE = "shouldBioAuthenticate";


    // singleton
    private static SettingsPrefs INSTANCE;
    private SharedPreferences sharedpreferences;

    private SettingsPrefs() {
        sharedpreferences = AppHolder.getContext().
                getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static SettingsPrefs getInstance() {
        SettingsPrefs instance = INSTANCE;
        if (instance == null) {
            synchronized (SettingsPrefs.class) {
                instance = INSTANCE;
                if (instance == null) {
                    instance = new SettingsPrefs();
                    INSTANCE = instance;
                }
            }
        }
        return INSTANCE;
    }

    // get / set by types
    public boolean getBooleanValueFromSharedPrefs(String key) {
        return sharedpreferences.getBoolean(key, true);
    }

    public void putBooleanValueToSharedPrefs(String key, boolean value) {
        this.sharedpreferences.edit().putBoolean(key, value).apply();
    }
}
