package com.ccube9.driver.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    private static final String APP_SETTINGS = "APP_SETTINGS";


    // properties
    private static  String IS_SIGN_UP = "sign_up";

    private static  String IS_LOGIN = "login";
    private static  String PROF_IMG = "prof_img";
    private static  String USER_ID = "user_id";
    private static  String USER_NAME = "user_name";
    private static  String USER_FIRST_NAME = "user_f_name";
    private static  String USER_LAST_NAME = "user_l_name";
    private static  String USER_MOBILE = "user_mobile";
    private static  String CITY = "city";
    private static  String COUNTRY = "country";
    private static String COMPANY_ID="company_id";
    private static String CAR_ID="car_id";
    private static String  TAKE_RIDE_STATUS="take_ride_status";

    private static  String API_TOKEN = "api_token";
    private static  String USER_EMAIL = "user_email";



    static PrefManager prefManager;
    private PrefManager()
    {

    }

    public static PrefManager getInstance(Context context)
    {
        if (prefManager==null)
        {
            prefManager=new PrefManager();
        }

        return prefManager;

    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
    }

    public static boolean IsSignUp(Context context) {
        return getSharedPreferences(context).getBoolean(IS_SIGN_UP , false);
    }

    public static void setSignUp(Context context, boolean newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(IS_SIGN_UP , newValue);
        editor.commit();
    }



    public static boolean IsLogin(Context context) {
        return getSharedPreferences(context).getBoolean(IS_LOGIN , false);
    }

    public static void setIsLogin(Context context, boolean newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(IS_LOGIN , newValue);
        editor.commit();
    }


    public static void setUserId(Context context, String user_id) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_ID , user_id);
        editor.commit();
    }

    public static String getUserId(Context context) {
        return getSharedPreferences(context).getString(USER_ID , "");
    }

    public static void setTakeRideStatus(Context context, String take_ride_status) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(TAKE_RIDE_STATUS , take_ride_status);
        editor.commit();
    }

    public static String getTakeRideStatus(Context context) {
        return getSharedPreferences(context).getString(TAKE_RIDE_STATUS , "");
    }

    public static void setCompanyId(Context context, String company_id) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(COMPANY_ID , company_id);
        editor.commit();
    }

    public static String getCompanyId(Context context) {
        return getSharedPreferences(context).getString(COMPANY_ID , "");
    }


    public static void setCarId(Context context, String car_id) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(CAR_ID , car_id);
        editor.commit();
    }

    public static String getCarId(Context context) {
        return getSharedPreferences(context).getString(CAR_ID , "");
    }



    public static void setCountry(Context context, String country) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(COUNTRY , country);
        editor.commit();
    }

    public static String getCountry(Context context) {
        return getSharedPreferences(context).getString(COUNTRY , "");
    }


    public static void setCity(Context context, String city) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(CITY , city);
        editor.commit();
    }

    public static String getCity(Context context) {
        return getSharedPreferences(context).getString(CITY , "");
    }


    public static void setUserName(Context context, String user_name) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_NAME , user_name);
        editor.commit();
    }

    public static String getUserName(Context context) {
        return getSharedPreferences(context).getString(USER_NAME , "");
    }
    public static void setUserFirstName(Context context, String user_first_name) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_FIRST_NAME , user_first_name);
        editor.commit();
    }

    public static String getUserFirstName(Context context) {
        return getSharedPreferences(context).getString(USER_FIRST_NAME , "");
    }





    public static void setUserLastName(Context context, String user_last_name) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_LAST_NAME , user_last_name);
        editor.commit();
    }

    public static String getUserLastName(Context context) {
        return getSharedPreferences(context).getString(USER_LAST_NAME , "");
    }


    public static void setUserEmail(Context context, String user_email) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_EMAIL , user_email);
        editor.commit();
    }

    public static String getUserEmail(Context context) {
        return getSharedPreferences(context).getString(USER_EMAIL , "");
    }

    public static void setUserMobile(Context context, String user_mobile) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_MOBILE , user_mobile);
        editor.commit();
    }
    public static void setProfImg(Context context, String profImg) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PROF_IMG , profImg);
        editor.commit();
    }

    public static void setApiToken(Context context,String apiToken) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(API_TOKEN , apiToken);
        editor.commit();
    }

    public static String getProfImg(Context context) {
        return getSharedPreferences(context).getString(PROF_IMG , "");
    }

    public static String getApiToken(Context context) {
        return getSharedPreferences(context).getString(API_TOKEN , "");
    }






    public static String getUserMobile(Context context) {
        return getSharedPreferences(context).getString(USER_MOBILE , "");
    }

    public static void LogOut(Context context)
    {
         getSharedPreferences(context).edit().clear().commit();
    }
}
