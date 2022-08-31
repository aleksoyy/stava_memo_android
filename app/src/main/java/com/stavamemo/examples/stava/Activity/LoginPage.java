package com.stavamemo.examples.stava.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stavamemo.examples.stava.API.APIRequestData;
import com.stavamemo.examples.stava.API.RetrofitHelper;
import com.stavamemo.examples.stava.Model.ResponseModel;
import com.stavamemo.examples.stava.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPage extends AppCompatActivity {

    EditText username, password;
    Button btn_login, btn_regist;

    String user, upass;
    int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_regist = (Button) findViewById(R.id.btn_regist);

        btn_login.setOnClickListener(view -> {

            user = username.getText().toString();
            upass = password.getText().toString();

            authLoginUser();
        });

        btn_regist.setOnClickListener(view -> {
            Intent reIntent = new Intent(LoginPage.this, RegisterPage.class);
            startActivity(reIntent);
        });
    }

    private void authLoginUser(){
        APIRequestData requestData = RetrofitHelper.conRetrofit().create(APIRequestData.class);
        Call<ResponseModel> authLoginData = requestData.authLogin(user, upass);
        authLoginData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int code = response.body().getCode();
                String message = response.body().getMessage();

                if (message.equals("User Available") && code == 1){
                    userId = response.body().getUid();
                    Intent maIntent = new Intent(LoginPage.this, MainActivity.class);
                    maIntent.putExtra("userId", userId);
                    startActivity(maIntent);
                }else{
                    Toast.makeText(LoginPage.this, "User Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(LoginPage.this, "Login Failed !"+t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}