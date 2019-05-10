package com.ccube9.driver.profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ccube9.driver.R;
import com.ccube9.driver.login.LoginActivity;
import com.ccube9.driver.network.APIClient;
import com.ccube9.driver.network.ApiInterface;
import com.ccube9.driver.network.BaseUrl;
import com.ccube9.driver.registration.SignUpActivity;
import com.ccube9.driver.registration.UserDetails;
import com.ccube9.driver.util.CustomUtil;
import com.ccube9.driver.util.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;

public class EditProfileActivity extends AppCompatActivity {
EditText et_f_name,et_l_name,et_mobile,et_country,et_city,et_email;
Button btn_update;
ImageView iv_back;
TextView tv_title;
private StringRequest stringRequest;
private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
         initView();
         requestQueue= Volley.newRequestQueue(this);
         btn_update.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 submitForm();
             }
         });
         iv_back.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(EditProfileActivity.this,ProfileActivity.class));
                 finish();
             }
         });

    }

    public void submitForm()
    {
        if (et_f_name.getText().toString().trim().length()==0)
        {
            et_f_name.setError(getResources().getString(R.string.enter_first_name));
            return;
        }

        if (et_l_name.getText().toString().trim().length()==0)
        {
            et_f_name.setError(getResources().getString(R.string.enter_last_name));
            return;
        }


        if (!isValidMobile(et_mobile.getText().toString().trim()))
        {
            if (et_mobile.getText().toString().trim().length()==0)
            {
                et_mobile.setError(getResources().getString(R.string.enter_mobile_no));
            }
            else
            {
                et_mobile.setError(getResources().getString(R.string.mobile_error));
            }

            return;
        }
        if (et_city.getText().toString().trim().length()==0)
        {
            et_city.setError(getResources().getString(R.string.enter_city));
            return;
        }
        if (et_country.getText().toString().trim().length()==0)
        {
            et_country.setError(getResources().getString(R.string.enter_country));
            return;
        }
        upDateData();

    }//submitFormClose
    private boolean isValidMobile(String phone) {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            if(phone.length() < 8 || phone.length() > 15) {
                // if(phone.length() != 10) {
                check = false;
                et_mobile.setError("Not Valid Number");
            } else {
                check = true;
            }
        } else {
            check=false;
        }
        return check;
    }

    public void upDateData()
    {
        CustomUtil.ShowDialog(EditProfileActivity.this);
//        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);
//        Call<UserProfile> call3 = apiInterface.update_user_data(et_email.getText().toString(),et_f_name.getText().toString(),et_l_name.getText().toString(),
//                et_mobile.getText().toString(),et_city.getText().toString(),et_country.getText().toString(),"en","2",
//                PrefManager.getUserId(this),PrefManager.getApiToken(this));



//        call3.enqueue(new Callback<UserProfile>() {
//            @Override
//            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
//                UserProfile userList = response.body();
//                String status = userList.status;
//
//                Log.d("fgfdgd",status);
//                if (status.equals("1"))
//                {
//                    String message = userList.message;
//                    Toast.makeText(EditProfileActivity.this, ""+message, Toast.LENGTH_SHORT).show();
//                    PrefManager.setUserLastName(EditProfileActivity.this,et_l_name.getText().toString());
//                    PrefManager.setUserFirstName(EditProfileActivity.this,et_f_name.getText().toString());
//                    PrefManager.setUserMobile(EditProfileActivity.this,et_mobile.getText().toString());
//
//                }
//
//                CustomUtil.DismissDialog(EditProfileActivity.this);
//            }
//
//            @Override
//            public void onFailure(Call<UserProfile> call, Throwable t) {
//                call.cancel();
//                CustomUtil.DismissDialog(EditProfileActivity.this);
//            }
//        });


        stringRequest=new StringRequest(Request.Method.POST, BaseUrl.BASE_URL.concat("driver/profile"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("sdsads",response);
                CustomUtil.DismissDialog(EditProfileActivity.this);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("status").equals("0")){
                        Toast.makeText(EditProfileActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    }
                   else if(jsonObject.getString("status").equals("1")){
                        Toast.makeText(EditProfileActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                        PrefManager.setUserFirstName(EditProfileActivity.this,jsonObject1.getString("name"));
                        PrefManager.setUserLastName(EditProfileActivity.this,jsonObject1.getString("last_name"));
                        PrefManager.setCity(EditProfileActivity.this,jsonObject1.getString("city"));
                        PrefManager.setCountry(EditProfileActivity.this,jsonObject1.getString("country"));
                        PrefManager.setProfImg(EditProfileActivity.this,jsonObject1.getString("profile_image"));
                        startActivity(new Intent(EditProfileActivity.this,ProfileActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                CustomUtil.DismissDialog(EditProfileActivity.this);
                requestQueue.cancelAll(stringRequest);

                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                if (message != null) {
                    Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(EditProfileActivity.this, "An error occured", Toast.LENGTH_SHORT).show();


            }
        }){



            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();


                param.put("name", et_f_name.getText().toString());
                param.put("last_name", et_l_name.getText().toString());
                param.put("email", et_email.getText().toString());
                param.put("phone", et_mobile.getText().toString());
                param.put("city", et_city.getText().toString());
                param.put("country", et_country.getText().toString());
                param.put("language", "en");
                param.put("user_type", "2");
                param.put("user_id", PrefManager.getUserId(EditProfileActivity.this));
                param.put("api_token", PrefManager.getApiToken(EditProfileActivity.this));

                return param;


            }




        };

        requestQueue.add(stringRequest);

    }
    public void initView()
    {
        btn_update=findViewById(R.id.btn_update);
        iv_back=findViewById(R.id.iv_back);
        tv_title=findViewById(R.id.tv_title);
        et_f_name=findViewById(R.id.et_f_name);
        et_l_name=findViewById(R.id.et_l_name);
        et_mobile=findViewById(R.id.et_mobile);
        tv_title.setText(getResources().getString(R.string.edit_profile));
        et_country=findViewById(R.id.et_country);
        et_city=findViewById(R.id.et_city);
        et_email=findViewById(R.id.et_email);

        et_f_name.setText(PrefManager.getUserFirstName(EditProfileActivity.this));
        et_l_name.setText(PrefManager.getUserLastName(EditProfileActivity.this));
        et_mobile.setText(PrefManager.getUserMobile(EditProfileActivity.this));
        et_email.setText(PrefManager.getUserEmail(EditProfileActivity.this));
        et_city.setText(PrefManager.getCity(EditProfileActivity.this));
        et_country.setText(PrefManager.getCountry(EditProfileActivity.this));

    }//initViewClose
}
