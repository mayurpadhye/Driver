package com.ccube9.driver.util;

import android.content.Context;

import com.ccube9.driver.R;

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


}
