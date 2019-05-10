package com.ccube9.driver.registration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ccube9.driver.R;
import com.ccube9.driver.login.LoginActivity;
import com.ccube9.driver.network.APIClient;
import com.ccube9.driver.network.ApiInterface;
import com.ccube9.driver.payment.AddPaymentActivity;
import com.ccube9.driver.util.CustomUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
Button btn_sign_up;
EditText et_f_name,et_l_name,et_email,et_mobile,et_password,et_c_password;
    TextView tv_title;
    ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(SignUpActivity.this,AddPaymentActivity.class));
                submitForm();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }//onCreateClose

    public void submitForm()
    {
        //  SignUp();
        if (isValidateFirstName(et_f_name.getText().toString().trim())==false)
        {
            if (et_l_name.getText().toString().trim().length()==0)
            {
                et_f_name.setError(getResources().getString(R.string.enter_first_name));
            }
            else
            {
                et_f_name.setError(getResources().getString(R.string.first_name_error));
            }

            return;
        }
        if (isValidateLastName(et_l_name.getText().toString().trim())==false)
        {
            if (et_l_name.getText().toString().trim().length()==0)
            {
                et_l_name.setError(getResources().getString(R.string.enter_last_name));
            }
            else
                et_l_name.setError(getResources().getString(R.string.last_name_error));
            return;
        }
        if (!isValidMail(et_email.getText().toString().trim()))
        {
            if (et_email.getText().toString().trim().length()==0)
            {
                et_email.setError(getResources().getString(R.string.enter_email_id));
            }
            else
            {
                et_email.setError(getResources().getString(R.string.email_error));
            }

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
        if (!validatePassword(et_password.getText().toString().trim()))
        {
            et_password.setError(getResources().getString(R.string.password_error));
            return;
        }
        if (!validateConfirmPassword(et_c_password.getText().toString().trim()))
        {
            if (et_c_password.getText().toString().trim().length()==0)
            {
                et_c_password.setError(getResources().getString(R.string.enter_confirm_pass));
            }
            else
                et_c_password.setError(getResources().getString(R.string.c_pass_error));
            return;
        }
        createUser();
    }

    private boolean isValidMail(String email) {
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(email);
        check = m.matches();

        if(!check) {
            et_email.setError("Not Valid Email");
        }
        return check;
    }
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
    public  boolean isValidateFirstName( String firstName )
    {
        if (firstName.trim().equals(""))
            return  false;
        else
            return firstName.matches("[a-zA-Z]*");

    } // end method validateFirstName

    // validate last name
    public  boolean isValidateLastName( String lastName )
    {
        if (lastName.trim().equals(""))
            return  false;
        else
            return lastName.matches("[a-zA-Z]*");
    }

    public boolean validatePassword( String password)
    {
        if (password.length()>0)
        {
            return true;
        }
        else
            return false;
    }

    public boolean validateConfirmPassword( String Cpassword)
    {
        if (Cpassword.length()>0 && Cpassword.equals(et_password.getText().toString()))
        {
            return true;
        }
        else
            return false;
    }
    public void createUser()
    {
        CustomUtil.ShowDialog(SignUpActivity.this);
       ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);
        Call<UserDetails> call3 = apiInterface.doCreateUserWithField(et_f_name.getText().toString().trim(),et_l_name.getText().toString(),et_email.getText().toString(),et_mobile.getText().toString(),et_password.getText().toString());
        call3.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                UserDetails userList = response.body();
                String status = userList.status;


               if (status.equals("1"))
               {
                   String message = userList.message;
                   Toast.makeText(SignUpActivity.this, ""+getResources().getString(R.string.registration_success), Toast.LENGTH_SHORT).show();
                   startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                   finish();

               }

                CustomUtil.DismissDialog(SignUpActivity.this);
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                call.cancel();
                CustomUtil.DismissDialog(SignUpActivity.this);
            }
        });

    }//createUser

      public void initView()
      {
          tv_title=findViewById(R.id.tv_title);
          iv_back=findViewById(R.id.iv_back);
          tv_title.setText(getResources().getString(R.string.create_an_account));
          et_c_password=findViewById(R.id.et_c_password);
          et_f_name=findViewById(R.id.et_f_name);
          et_l_name=findViewById(R.id.et_l_name);
          et_email=findViewById(R.id.et_email);
          et_mobile=findViewById(R.id.et_mobile);
          et_password=findViewById(R.id.et_password);
          btn_sign_up=findViewById(R.id.btn_sign_up);

      }//initViewClose
}
