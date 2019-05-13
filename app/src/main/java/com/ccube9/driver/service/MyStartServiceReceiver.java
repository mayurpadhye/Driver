package com.ccube9.driver.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.ccube9.driver.util.CustomUtil;

public class MyStartServiceReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "this", Toast.LENGTH_SHORT).show();
        Log.i("Broadcast Listened", "Service tried to stop");
        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, DriverLatLangService.class));
        } else {
            context.startService(new Intent(context, DriverLatLangService.class));
        }
        //CustomUtil.scheduleJob(context);
    }
}
