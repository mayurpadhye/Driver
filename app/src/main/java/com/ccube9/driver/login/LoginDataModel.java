package com.ccube9.driver.login;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginDataModel {
   public String status;
   public String message;
    @SerializedName("user_data")
    public List<UserData> data = null;
    public class UserData
    {
        @SerializedName("id")
        public String user_id;
        public String first_name;
        public String last_name;
        public String email_id;
        public String phone_no;
        public String created_date;
    }
}
