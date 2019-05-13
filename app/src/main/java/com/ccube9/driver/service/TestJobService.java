package com.ccube9.driver.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.ccube9.driver.util.CustomUtil;

import okhttp3.internal.Util;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class TestJobService extends JobService {
    private static final String TAG = "SyncService";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onStartJob(JobParameters params) {
        Intent service = new Intent(getApplicationContext(), DriverLatLangService.class);
        getApplicationContext().startService(service);
        CustomUtil.scheduleJob(getApplicationContext()); // reschedule the job
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
