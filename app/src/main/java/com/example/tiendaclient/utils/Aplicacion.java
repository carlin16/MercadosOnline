package com.example.tiendaclient.utils;

import android.app.Application;

import com.google.firebase.analytics.FirebaseAnalytics;



public class Aplicacion extends Application {
    public static FirebaseAnalytics mFirebaseAnalytics;


    @Override
    public void onCreate() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        super.onCreate();
    }
}
