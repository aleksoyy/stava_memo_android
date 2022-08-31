package com.stavamemo.examples.stava.Activity;

import androidx.appcompat.app.AppCompatActivity;

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

    TextView mid, userid, memoDate;
    EditText memoTitle, memoContains;

    String mdate, mtitle, mcont;
    int usid, userId;
    int exuid, exmid;
    String exmtitle, exmdate, exmcont;

    FloatingActionButton btn_save;

    String tempTitle, tempCont;

    private Timer timer = new Timer();
    private final long DELAY = 1000; // in ms
    public static final String LAST_TEXT_TITLE = "";
    public static final String LAST_TEXT_CONT = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_page);

        Bundle bundle = getIntent().getExtras();

        //From AddButton MainActivity
        userId = bundle.getInt("userId");

        //From AdapterData
        exmid = bundle.getInt("mid");
        exuid = bundle.getInt("uid");
        exmtitle = bundle.getString("mtitle");
        exmdate = bundle.getString("mdate");
        exmcont = bundle.getString("mcont");

        //Get layout contain
        mid = (TextView) findViewById(R.id.memoId);
        userid = (TextView) findViewById(R.id.uid);
        memoDate = (TextView) findViewById(R.id.memoDate);
        memoTitle = (EditText) findViewById(R.id.memoTitle);
        memoContains = (EditText) findViewById(R.id.memoContain);
        btn_save = (FloatingActionButton) findViewById(R.id.btn_save);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        mtitle = memoTitle.getText().toString();

        //Setting Date
        mdate = formattedDate;
        memoDate.setText(mdate);

        mcont = memoContains.getText().toString();
        usid = userId;

        if (exmid == 0 && exuid == 0 && exmtitle.equals("") && exmdate.equals("") && exmcont.equals("")){
            if (mtitle.trim().equals("") && mcont.trim().equals("")){
                memoTitle.setHint("Title Memo");
                memoContains.setHint("Hello you can type anything you want here !");
            } else if(mtitle.trim().equals("")){
                memoTitle.setHint("Title Memo");
            } else if(mcont.trim().equals("")) {
                memoTitle.setHint("Hello you can type anything you want here !");
            }
        }else{
            mid.setText(exmid);
            userid.setText(exuid);
            memoTitle.setText(exmtitle);
            memoContains.setText(exmcont);
        }

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (exmid == 0 && exuid == 0 && exmtitle.equals("") && exmdate.equals("") && exmcont.equals("")) {
                    createMemo();
                }else{
                    updateMemo();
                }
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (memoTitle.getText() != null  || memoContains.getText() != null ) {
                    updateMemo();
                }
            }
        }, 1000);
    }

    private void createMemo(){
        APIRequestData requestData = RetrofitHelper.conRetrofit().create(APIRequestData.class);
        Call<ResponseModel> uploadData = requestData.uploadDataModel(mtitle, mdate, mcont, usid);
        uploadData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int code = response.body().getCode();
                String message = response.body().getMessage();
               }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Memo Failed to Add"+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateMemo(){
        APIRequestData requestData = RetrofitHelper.conRetrofit().create(APIRequestData.class);
        Call<ResponseModel> updateMemoData = requestData.updatMemoData(mtitle, mdate, mcont);
        updateMemoData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int code = response.body().getCode();
                String message = response.body().getMessage();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(MemoPage.this, "Failed to update memo : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}