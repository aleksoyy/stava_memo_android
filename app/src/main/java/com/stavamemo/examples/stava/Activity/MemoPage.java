package com.stavamemo.examples.stava.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.stavamemo.examples.stava.API.APIRequestData;
import com.stavamemo.examples.stava.API.RetrofitHelper;
import com.stavamemo.examples.stava.Model.ResponseModel;
import com.stavamemo.examples.stava.R;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemoPage extends AppCompatActivity {

    TextView memoid, userid, memoDate;
    EditText memoTitle, memoContains;

    String memodate, mdate, mtitle, mcont;
    String newMdate, newMtitle, newMcont;
    int uid, mid;

    FloatingActionButton btn_save;

    private Timer timer = new Timer();
    private final long DELAY = 1000; // in ms
    public static final String LAST_TEXT_TITLE = "";
    public static final String LAST_TEXT_CONT = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_page);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            //From AdapterData
            mid = Integer.parseInt(bundle.getString("mid"));
            uid = Integer.parseInt(bundle.getString("uid"));
        }
        //Get layout contain
        memoid = (TextView) findViewById(R.id.memoId);
        userid = (TextView) findViewById(R.id.uid);
        memoDate = (TextView) findViewById(R.id.memoDate);
        memoTitle = (EditText) findViewById(R.id.memoTitle);
        memoContains = (EditText) findViewById(R.id.memoContain);
        btn_save = (FloatingActionButton) findViewById(R.id.btn_save);

        getSpecMemo();

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                String formattedDate = df.format(c);
                memodate = formattedDate;

                newMtitle = memoTitle.getText().toString();
                newMdate = memodate;
                newMcont = memoContains.getText().toString();

                memoDate.setText(memodate);
                updateMemo();
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                String formattedDate = df.format(c);
                memodate = formattedDate;

                newMtitle = memoTitle.getText().toString();
                newMdate = memodate;
                newMcont = memoContains.getText().toString();

                memoDate.setText(memodate);
                updateMemo();
            }
        }, 1000);
    }

    public void onBackPressed(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        memodate = formattedDate;

        newMtitle = memoTitle.getText().toString();
        newMdate = memodate;
        newMcont = memoContains.getText().toString();

        memoDate.setText(memodate);
        updateMemo();
        Intent backIntent = new Intent(MemoPage.this, MainActivity.class);
        startActivity(backIntent);
    }

    private void getSpecMemo(){
        APIRequestData requestData = RetrofitHelper.conRetrofit().create(APIRequestData.class);
        Call<ResponseModel> getSpecMemo = requestData.getSpecMemo(mid, uid);
        getSpecMemo.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int code = response.body().getCode();
                String message = response.body().getMessage();

                memoTitle.setText(response.body().getData().get(0).getMtitle());
                memoContains.setText(response.body().getData().get(0).getMcont());
                memoDate.setText(response.body().getData().get(0).getMdate());
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(MemoPage.this, "Memo Not Found : "+t.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateMemo(){
        APIRequestData updateData = RetrofitHelper.conRetrofit().create(APIRequestData.class);
        Call<ResponseModel> updateMemoData = updateData.updatMemoData(mid, newMtitle, newMdate, newMcont, uid);
        updateMemoData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int code = response.body().getCode();
                String message = response.body().getMessage();
                Toast.makeText(MemoPage.this, "Memo Saved !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(MemoPage.this, "Failed to update memo : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}