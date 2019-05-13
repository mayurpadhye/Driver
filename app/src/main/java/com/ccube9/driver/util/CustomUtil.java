package com.ccube9.driver.util;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.ccube9.driver.R;
import com.ccube9.driver.service.TestJobService;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CustomUtil {

  static   SweetAlertDialog dialog;
    public static void ShowDialog(Context context)
    {

            dialog=new SweetAlertDialog(context);
            dialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
            dialog.setTitleText(context.getResources().getString(R.string.loading));
            dialog.show();



    }

    public static void DismissDialog(Context context)
    {
        if (dialog.isShowing())
            dialog.dismiss();
    }
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, TestJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(1 * 1000); // wait at least
        builder.setOverrideDeadline(3 * 1000); // maximum delay
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        builder.setRequiresDeviceIdle(true); // device should be idle
        builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
    }

}
