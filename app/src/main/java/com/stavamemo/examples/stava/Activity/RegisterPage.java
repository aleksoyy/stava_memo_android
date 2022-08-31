package com.stavamemo.examples.stava.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.stavamemo.examples.stava.API.APIRequestData;
import com.stavamemo.examples.stava.API.RetrofitHelper;
import com.stavamemo.examples.stava.Model.ResponseModel;
import com.stavamemo.examples.stava.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPage extends AppCompatActivity {
    EditText fullname, username, password, cPassword, email;
    Button btn_reg, btn_cancel;

    String mUsername, mName, mPassword, cmPassword, mEmail;
    String mtitle, mdate, mcont;
    int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        fullname = findViewById(R.id.fullname);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        cPassword = findViewById(R.id.confirm_password);
        email = findViewById(R.id.email);
        btn_reg = findViewById(R.id.btn_reg);
        btn_cancel = findViewById(R.id.btn_cancel);

        btn_reg.setOnClickListener(view -> {
            mName = fullname.getText().toString();
            mUsername = username.getText().toString();
            mPassword = password.getText().toString();
            cmPassword = cPassword.getText().toString();
            mEmail = email.getText().toString();

            if (mPassword.equals(cmPassword)){
                regisUser();
                Intent toLoginIntent = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(toLoginIntent);
            }else{
                Toast.makeText(getApplicationContext(), "Password not match", Toast.LENGTH_LONG).show();
            }
        });
    }

    //Function
    public void regisUser(){
        APIRequestData requestData = RetrofitHelper.conRetrofit().create(APIRequestData.class);
        Call<ResponseModel> registUserAccount = requestData.authregis( mUsername, mName, mPassword, mEmail);
        registUserAccount.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int code = response.body().getCode();
                String message = response.body().getMessage();
                getUserIdData();
                Toast.makeText(RegisterPage.this, "Registration Success !!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(RegisterPage.this, "Registration Failed | Msg : "+t, Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void getUserIdData(){
        APIRequestData reqUidData = RetrofitHelper.conRetrofit().create(APIRequestData.class);
        Call<ResponseModel> authLoginData = reqUidData.authLogin(mUsername, mPassword);
        authLoginData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int code = response.body().getCode();
                String message = response.body().getMessage();

                if (message.equals("User Available") && code == 1){
                    uid = response.body().getUid();
                    createMemo();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
            }
        });
    }

    private void createMemo() {
        mtitle = "Hello Memo from Stava !";
        //Setting Date
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        mdate = formattedDate;
        mcont = "Welcome, maybe this is your first time using Stava.\nI greet you from heavenly greet! \nSome tips, Add Memo available at right the bottom of this page and you can swipe down this page to refresh it!\nTell me more about your story or task !";

        APIRequestData reqCreateMemo = RetrofitHelper.conRetrofit().create(APIRequestData.class);
        Call<ResponseModel> uploadData = reqCreateMemo.uploadDataModel(mtitle, mdate, mcont, uid);
        uploadData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int code = response.body().getCode();
                String message = response.body().getMessage();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
            }
        });
    }


}