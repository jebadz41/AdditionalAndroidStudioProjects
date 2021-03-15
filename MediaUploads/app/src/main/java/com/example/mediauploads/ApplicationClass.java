package com.example.mediauploads;

import android.app.Application;

import com.backendless.Backendless;

public class ApplicationClass extends Application {

    public static final String APPLICATION_ID = "14424E90-27B2-47C6-FFE6-FFCBA79AA500";
    public static final String API_KEY = "99227B82-06D8-4DA3-B318-AB7E00F2A6C7";
    public static final String SERVER_URL = "https://eu-api.backendless.com";

    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.initApp(getApplicationContext(), APPLICATION_ID, API_KEY);
        Backendless.setUrl(SERVER_URL);

    }
}
