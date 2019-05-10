package com.ccube9.driver.network;

import com.ccube9.driver.login.LoginDataModel;
import com.ccube9.driver.profile.UserProfile;
import com.ccube9.driver.registration.UserDetails;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("/cabbooking/app/User/user_registration")
    Call<UserDetails> doCreateUserWithField(@Field("first_name") String first_name,
                                            @Field("last_name") String last_name,
                                            @Field("email_id") String email_id,
                                            @Field("phone_number") String phone_number,
                                            @Field("password") String password);

    @FormUrlEncoded
    @POST("/cabbooking/app/User/user_login")
    Call<LoginDataModel> doUserLogin(
                                     @Field("email_id") String email_id,
                                     @Field("password") String password);

    @FormUrlEncoded
    @POST(" Profile_update")
    Call<UserProfile> update_user_data(
            @Field("email") String email,
            @Field("name") String name,
            @Field("last_name") String last_name,
            @Field("phone") String phone,
            @Field("city") String city,
            @Field("country") String country,
            @Field("language") String lang,
            @Field("user_type") String user_type,
            @Field("user_id") String user_id,
            @Field("api_token") String token

            );





    @FormUrlEncoded
    @POST("/cabbooking/app/User/change_password")
    Call<UserProfile> change_password(
            @Field("user_id") String user_id,
            @Field("new_password") String new_password,
            @Field("old_password") String old_password);
}
