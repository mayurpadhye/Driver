package com.ccube9.driver.MyCar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.ccube9.driver.home.HomeActivity;
import com.ccube9.driver.login.LoginActivity;
import com.ccube9.driver.network.BaseUrl;
import com.ccube9.driver.profile.ProfileActivity;
import com.ccube9.driver.util.CustomUtil;
import com.ccube9.driver.util.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyCarActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener {
    private TextView tv_iv_title,txtvewcarno;
    private ImageView iv_back;
    private RequestQueue requestQueue;
    private StringRequest stringRequest,stringRequest1,stringRequest2;
    private ArrayList<String> cab_number;
    private Spinner cab_no_spinner;
    private Button btn_select_car;
    private JSONArray jsonArrayCabdata;
    private String cab_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_car);
        initView();

        requestQueue= Volley.newRequestQueue(MyCarActivity.this);
        cab_number=new ArrayList<>();
        cab_no_spinner.setOnItemSelectedListener(this);

        tv_iv_title.setText("My Car");
        onClick();

if(!PrefManager.getCarId(MyCarActivity.this).equals("null")) {
    stringRequest2 = new StringRequest(Request.Method.POST, BaseUrl.BASE_URL.concat("driver/single_car"), new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d("firstsadsd", response);

            try {
                JSONObject jsonObject1=new JSONObject(response);
               if( jsonObject1.getString("status").equals("1")){
                   JSONObject jsonObject11=jsonObject1.getJSONObject("data");
                   txtvewcarno.setText(jsonObject11.getString("cab_number"));
                   jsonObject11.getString("id");
               }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {

            requestQueue.cancelAll(stringRequest2);
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
                Toast.makeText(MyCarActivity.this, message, Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(MyCarActivity.this, "An error occured", Toast.LENGTH_SHORT).show();
        }
    }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();

            params.put("api_token", PrefManager.getApiToken(MyCarActivity.this));
            params.put("user_id", PrefManager.getUserId(MyCarActivity.this));
            params.put("car_id", PrefManager.getCarId(MyCarActivity.this));
            params.put("language", "en");
            params.put("user_type", "2");

            return params;
        }
    };

    requestQueue.add(stringRequest2);

}

        stringRequest=new StringRequest(Request.Method.POST, BaseUrl.BASE_URL.concat("driver/car_list"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("sadsd",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                  if(jsonObject.getString("status").equals("1")){

                      jsonArrayCabdata=jsonObject.getJSONArray("data");
                      for (int i=0;i<jsonArrayCabdata.length();i++) {
                          JSONObject jsonObject1 = jsonArrayCabdata.getJSONObject(i);
                          cab_number.add(jsonObject1.getString("cab_number"));
                      }
                      cab_no_spinner.setAdapter(new ArrayAdapter<String>(MyCarActivity.this, android.R.layout.simple_spinner_dropdown_item, cab_number));
                  }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

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
                    Toast.makeText(MyCarActivity.this, message, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(MyCarActivity.this, "An error occured", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("api_token", PrefManager.getApiToken(MyCarActivity.this));
                params.put("user_id", PrefManager.getUserId(MyCarActivity.this));
                params.put("company_id", PrefManager.getCompanyId(MyCarActivity.this));
                params.put("language", "en");
                params.put("user_type", "2");

                return params;
            }
        };

        requestQueue.add(stringRequest);






    }


    private String getCabId(int position){
        String Cabid="";
        try {
            JSONObject json5 = jsonArrayCabdata.getJSONObject(position);

            Cabid = json5.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return Cabid;
    }

    private void onClick(){
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyCarActivity.this, HomeActivity.class));

            }
        });

        btn_select_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cab_id.equals("")) {

                    CustomUtil.ShowDialog(MyCarActivity.this);
                     stringRequest1 = new StringRequest(Request.Method.POST, BaseUrl.BASE_URL.concat("driver/select_car"), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("sadsd", response);
                            CustomUtil.DismissDialog(MyCarActivity.this);

                            try {
                                JSONObject jsonObject1=new JSONObject(response);
                               if( jsonObject1.getString("status").equals("1")){
                                   Toast.makeText(MyCarActivity.this, jsonObject1.getString("message"), Toast.LENGTH_SHORT).show();
                                   PrefManager.setCarId(MyCarActivity.this,jsonObject1.getString("car_id"));
                                   startActivity(new Intent(MyCarActivity.this,MyCarActivity.class));
                                }
                                else if( jsonObject1.getString("status").equals("0")){
                                   Toast.makeText(MyCarActivity.this,  jsonObject1.getString("message"), Toast.LENGTH_SHORT).show();
                               }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            CustomUtil.DismissDialog(MyCarActivity.this);

                            requestQueue.cancelAll(stringRequest1);
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
                                Toast.makeText(MyCarActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(MyCarActivity.this, "An error occured", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams()throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();


                            params.put("api_token", PrefManager.getApiToken(MyCarActivity.this));
                            params.put("user_id", PrefManager.getUserId(MyCarActivity.this));
                            params.put("car_id", cab_id);
                            params.put("language", "en");
                            params.put("user_type", "2");

                            return params;
                        }
                    };

                    requestQueue.add(stringRequest1);
                }
                else{
                    Toast.makeText(MyCarActivity.this, "Please select car", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    private void initView(){
        tv_iv_title=findViewById(R.id.tv_title);
        iv_back=findViewById(R.id.iv_back);
        cab_no_spinner=findViewById(R.id.cab_no_spinner);
        btn_select_car=findViewById(R.id.btn_select_car);
        txtvewcarno=findViewById(R.id.txtvewcarno);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner= (Spinner) parent;

        cab_id = getCabId(position);
            

            Log.d("gfdgfg",cab_id );

        
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
