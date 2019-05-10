package com.ccube9.driver.registration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ccube9.driver.R;
import com.ccube9.driver.login.LoginActivity;

public class ChooseRegistrationRoleActivity extends AppCompatActivity {
Button
        //btn_create_account,
       btn_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_registration_role);
       // btn_create_account=findViewById(R.id.btn_create_account);
        btn_login=findViewById(R.id.btn_login);
//        btn_create_account.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(ChooseRegistrationRoleActivity.this,SignUpActivity.class));
//                finish();
//            }
//        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseRegistrationRoleActivity.this,LoginActivity.class));
                finish();
            }
        });
    }
}
