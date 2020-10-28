package com.mercadoonline.tiendaclient.utils;

import android.app.Application;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class Aplicacion extends Application {
    public static FirebaseAnalytics mFirebaseAnalytics;
    public static FirebaseCrashlytics mFirebaseCrashlytics;



    @Override
    public void onCreate() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        mFirebaseCrashlytics=FirebaseCrashlytics.getInstance();
        mFirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        super.onCreate();
    }



}
