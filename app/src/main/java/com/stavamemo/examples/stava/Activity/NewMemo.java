package com.stavamemo.examples.stava.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class NewMemo extends AppCompatActivity {

    EditText memoTitle, memoContain;
    TextView memoDate;
    FloatingActionButton btn_save;

    String mtitle, mdate, mcont;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_memo);

        Bundle bundle = getIntent().getExtras();
        userId = bundle.getInt("userId");

        memoTitle = findViewById(R.id.memoTitle);
        memoDate = findViewById(R.id.memoDate);
        memoContain = findViewById(R.id.memoContain);
        btn_save = findViewById(R.id.btn_save);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        mdate = formattedDate;
        memoDate.setText(mdate);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mtitle = memoTitle.getText().toString();
                mcont = memoContain.getText().toString();
                createMemo();
            }
        });


    }

    private void createMemo(){
        APIRequestData requestData = RetrofitHelper.conRetrofit().create(APIRequestData.class);
        Call<ResponseModel> uploadData = requestData.uploadDataModel(mtitle, mdate, mcont, userId);
        uploadData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int code = response.body().getCode();
                String message = response.body().getMessage();
                Intent backIntent = new Intent(NewMemo.this, MainActivity.class);
                Toast.makeText(NewMemo.this, "Memo Saved !", Toast.LENGTH_SHORT).show();
                startActivity(backIntent);
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Memo Failed to Add"+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}