package com.ccube9.driver.profile;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
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
import com.ccube9.driver.home.HomeActivity;
import com.ccube9.driver.network.APIClient;
import com.ccube9.driver.network.ApiInterface;
import com.ccube9.driver.network.BaseUrl;
import com.ccube9.driver.util.CustomUtil;
import com.ccube9.driver.util.PrefManager;
import com.ccube9.driver.util.VolleyMultipartRequest;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class ProfileActivity extends AppCompatActivity {

    TextView tv_change,tv_f_name,tv_l_name,tv_email,tv_mobile,tv_title,et_city,et_country;
    ImageView iv_back,profpic;
    FloatingActionButton fb_edit;
    Dialog change_pass_dialog;
    EditText et_old_password,et_new_pass,et_c_password;
    Button btn_change_pass;
    private RequestQueue requestQueue;
    private   VolleyMultipartRequest volleyMultipartRequest;
    private StringRequest stringRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initView();
        requestQueue= Volley.newRequestQueue(this);
        String img=PrefManager.getProfImg(ProfileActivity.this);
        Log.d("dffddsf",img);
        if(img!=null || !img.equals(" ")){
            Picasso.with(this).load(BaseUrl.IMAGE_URL.concat(img)).into(profpic);
        }else{
            Picasso.with(this).load("http://driver.ccube9.com/public/profile/placeholder-profile.jpg").fit().into(profpic);
        }
        onClick();


    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
        }

    private void setData() {
      tv_f_name.setText(PrefManager.getUserFirstName(ProfileActivity.this));
      tv_l_name.setText(PrefManager.getUserLastName(ProfileActivity.this));
      tv_email.setText(PrefManager.getUserEmail(ProfileActivity.this));
      tv_mobile.setText(PrefManager.getUserMobile(ProfileActivity.this));
      et_city.setText(PrefManager.getCity(ProfileActivity.this));
      et_country.setText(PrefManager.getCountry(ProfileActivity.this));

    }

    private void onClick() {

      iv_back.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              startActivity(new Intent(ProfileActivity.this,HomeActivity.class));
              finish();
          }
      });
        fb_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,EditProfileActivity.class));
            }
        });

        profpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkPermission()==false){
                    requestPermission();
                }else {

                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 1);

                }

            }
        });





        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_pass_dialog=new Dialog(ProfileActivity.this);
                change_pass_dialog.setContentView(R.layout.dialog_change_password);
                et_old_password=change_pass_dialog.findViewById(R.id.et_old_password);
                et_new_pass=change_pass_dialog.findViewById(R.id.et_new_pass);
                et_c_password=change_pass_dialog.findViewById(R.id.et_c_password);
                btn_change_pass=change_pass_dialog.findViewById(R.id.btn_change_pass);
                change_pass_dialog.show();

                btn_change_pass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        passValidation();
                    }
                });
            }
        });

    }


    public void passValidation()
    {

        if (et_old_password.getText().toString().trim().length()==0)
        {
            et_old_password.setError(getResources().getString(R.string.enter_old_pass));
            return;
        }
        else if(et_old_password.getText().toString().trim().length()<8) {
            et_old_password.setError(getResources().getString(R.string.eight_cchar_pass));
        }

        if (et_new_pass.getText().toString().trim().length()==0)
        {
            et_new_pass.setError(getResources().getString(R.string.enter_new_pass));
            return;

        }
        else if(et_new_pass.getText().toString().trim().length()<8) {
            et_new_pass.setError(getResources().getString(R.string.eight_cchar_pass));
        }
        if (et_c_password.getText().toString().trim().length()==0)
        {
            et_c_password.setError(getResources().getString(R.string.enter_confirm_pass));
            return;
        }
        else if(et_c_password.getText().toString().trim().length()<8){
            et_c_password.setError(getResources().getString(R.string.eight_cchar_pass));}

          else  if (!et_new_pass.getText().toString().trim().equals(et_c_password.getText().toString().trim()))
            {
                Toast.makeText(this, ""+getResources().getString(R.string.pass_not_matched), Toast.LENGTH_SHORT).show();
                 return;




        }
        else


        changePassword();
    }

    public void changePassword()
    {

        CustomUtil.ShowDialog(ProfileActivity.this);

//        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);
//        Call<UserProfile> call3 = apiInterface.change_password(PrefManager.getUserId(ProfileActivity.this),et_new_pass.getText().toString().trim(),et_old_password.getText().toString());
//        call3.enqueue(new Callback<UserProfile>() {
//            @Override
//            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
//                UserProfile userList = response.body();
//                String status = userList.status;
//
//
//                if (status.equals("1"))
//                {
//                    String message = userList.message;
//                    Toast.makeText(ProfileActivity.this, ""+message, Toast.LENGTH_SHORT).show();
//
//                }
//                else
//                {
//                    String message = userList.message;
//                    Toast.makeText(ProfileActivity.this, ""+message, Toast.LENGTH_SHORT).show();
//
//                }
//                change_pass_dialog.dismiss();
//                CustomUtil.DismissDialog(ProfileActivity.this);
//            }
//
//            @Override
//            public void onFailure(Call<UserProfile> call, Throwable t) {
//                call.cancel();
//                change_pass_dialog.dismiss();
//                CustomUtil.DismissDialog(ProfileActivity.this);
//            }
//        });

        stringRequest =new StringRequest(Request.Method.POST, BaseUrl.BASE_URL.concat("driver/change"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomUtil.DismissDialog(ProfileActivity.this);
                Log.d("sads",response);
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(response);
                   if(jsonObject.getString("status").equals("0")){

                       Toast.makeText(ProfileActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                   }
                    else if(jsonObject.getString("status").equals("1")){
                       Toast.makeText(ProfileActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                       change_pass_dialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomUtil.DismissDialog(ProfileActivity.this);

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
                    Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(ProfileActivity.this, "An error occured", Toast.LENGTH_SHORT).show();


            }
        }){



            @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<>();

                param.put("language","en");
                param.put("user_type","2");
                param.put("old_password",et_old_password.getText().toString());
                param.put("password",et_new_pass.getText().toString());
                param.put("password_confirmation",et_c_password.getText().toString());
                param.put("user_id",PrefManager.getUserId(ProfileActivity.this));
                param.put("api_token",PrefManager.getApiToken(ProfileActivity.this));

                return param;}


        };

        requestQueue.add(stringRequest);
    }//changePaswordClose


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {


            try {
                //getting bitmap object from uri
              final Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());

                //displaying selected image to imageview
                profpic.setImageBitmap(bitmap);

                //calling the method uploadBitmap to upload image


                CustomUtil.ShowDialog(ProfileActivity.this);
                  volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, BaseUrl.BASE_URL.concat("driver/profile_image_update"),
                        new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {

                                CustomUtil.DismissDialog(ProfileActivity.this);
                                Log.d("dsadd", String.valueOf(response.statusCode));
                                if (response.statusCode == 200) {
                                    String result = new String(response.data);
                                    try {
                                        JSONObject response1 = new JSONObject(result);
                                        Log.d("dsadsa", String.valueOf(response1));
                                        if(response1.getString("status").equals("0")){
                                            Toast.makeText(ProfileActivity.this, response1.getString("message"), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(ProfileActivity.this,ProfileActivity.class));
                                        }
                                        else if(response1.getString("status").equals("1")){
                                            Toast.makeText(ProfileActivity.this, response1.getString("message"), Toast.LENGTH_LONG).show();
                                        JSONObject jsonObject2=response1.getJSONObject("data");
                                        PrefManager.setProfImg(ProfileActivity.this,jsonObject2.getString("profile_image"));
                                        startActivity(new Intent(ProfileActivity.this,ProfileActivity.class));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                CustomUtil.DismissDialog(ProfileActivity.this);
                                //volleyError.printStackTrace();
                                requestQueue.cancelAll(volleyMultipartRequest);

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
                                    Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(ProfileActivity.this, "An error occured", Toast.LENGTH_SHORT).show();

                                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {


                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("language", "en");
                        param.put("user_type", "2");
                        param.put("user_id", PrefManager.getUserId(ProfileActivity.this));
                        param.put("api_token", PrefManager.getApiToken(ProfileActivity.this));
                        return param;
                    }
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("Accept", "application/json");

                        return params;
                    }
                    /*
                     * Here we are passing image by renaming it with a unique name
                     * */
                    @Override
                    protected Map<String, VolleyMultipartRequest.DataPart> getByteData() throws  AuthFailureError {
                        Map<String, DataPart> params = new HashMap<>();

                            long imagename = System.currentTimeMillis();

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 30, byteArrayOutputStream);
                            if(bitmap. getHeight()>1200 || bitmap.getWidth()>1920) {


                                params.put("profile_image", new VolleyMultipartRequest.DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                            }
                            else{
                                params.put("profile_image", new VolleyMultipartRequest.DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                            }


                        return params;
                    }
                };

                requestQueue.add(volleyMultipartRequest);





            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 30, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    public void initView()
    {



        tv_change=findViewById(R.id.tv_change);
        fb_edit=findViewById(R.id.fb_edit);
        tv_f_name=findViewById(R.id.tv_f_name);
        tv_l_name=findViewById(R.id.tv_l_name);
        tv_email=findViewById(R.id.tv_email);
        tv_mobile=findViewById(R.id.tv_mobile);
        tv_title=findViewById(R.id.tv_title);
        iv_back=findViewById(R.id.iv_back);
        tv_title.setText(getResources().getString(R.string.profile));
        profpic=findViewById(R.id.profpic);
        et_city=findViewById(R.id.et_city);;
        et_country=findViewById(R.id.et_country);;


    }//initViewClose


    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
        } else {
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    private boolean checkPermission() {

        int result3 = (ActivityCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE));

        if (result3 == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


}
