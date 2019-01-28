package com.speedrocket.progmine.speedrocket.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class AnalyticsService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        Toast.makeText(getApplicationContext() , " onCreate Service " , Toast.LENGTH_LONG).show();
        super.onCreate();
    }

    @Override
    public void onDestroy() {

        Toast.makeText(getApplicationContext() , " onDestroy Service " , Toast.LENGTH_LONG).show();
        super.onDestroy();
    }


}
