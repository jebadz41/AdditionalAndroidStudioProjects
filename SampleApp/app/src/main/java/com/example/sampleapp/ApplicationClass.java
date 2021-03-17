package com.example.sampleapp;

import android.app.Application;

import com.backendless.Backendless;

public class ApplicationClass extends Application {

    public static final String APPLICATION_ID = "8A37051F-160C-6215-FFC5-C90687EBC300";
    public static final String API_KEY = "FFDCF47C-2CA7-4210-A9EE-D405413131D1";
    public static final String SERVER_URL = "https://eu-api.backendless.com";

    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.initApp(getApplicationContext(), APPLICATION_ID, API_KEY);
        Backendless.setUrl(SERVER_URL);

    }
}
