package com.stavamemo.examples.stava.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.stavamemo.examples.stava.API.APIRequestData;
import com.stavamemo.examples.stava.API.RetrofitHelper;
import com.stavamemo.examples.stava.Adapter.AdapterData;
import com.stavamemo.examples.stava.Model.MemoModel;
import com.stavamemo.examples.stava.Model.ResponseModel;
import com.stavamemo.examples.stava.R;
import com.stavamemo.examples.stava.databinding.ActivityMainBinding;

import java.net.ResponseCache;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvMemoData;
    private RecyclerView.Adapter adMemoData;
    private RecyclerView.LayoutManager lmMemoData;
    private SwipeRefreshLayout refreshLayout;
    private ProgressBar progressBar;

    private List<MemoModel> listMemoData = new ArrayList<>();

    FloatingActionButton btn_add;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();
        userId = bundle.getInt("userId");

        rvMemoData = findViewById(R.id.mainList);
        refreshLayout = findViewById(R.id.refreshLayout);
        progressBar = findViewById(R.id.progBar);

        lmMemoData = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMemoData.setLayoutManager(lmMemoData);
        retrieveData();

        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(true);
            retrieveData();
            refreshLayout.setRefreshing(false);
        });

        btn_add = (FloatingActionButton) findViewById(R.id.btn_addMemo);
        btn_add.setOnClickListener(view -> {
            Intent addIntent = new Intent(MainActivity.this, MemoPage.class);
            addIntent.putExtra("userId", userId);
            startActivity(addIntent);
        });
    }

    public void retrieveData (){
        progressBar.setVisibility(View.VISIBLE);
        APIRequestData requestData = RetrofitHelper.conRetrofit().create(APIRequestData.class);
        Call<ResponseModel> fetchData = requestData.fetchDataModel(userId);
        fetchData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int code = response.body().getCode();
                String message = response.body().getMessage();

                Toast.makeText(MainActivity.this, "Code :" +code+" | Msg : "+message+userId, Toast.LENGTH_SHORT).show();

                listMemoData = response.body().getData();
                adMemoData = new AdapterData(MainActivity.this, listMemoData);
                rvMemoData.setAdapter(adMemoData);
                adMemoData.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to connect with server."+t.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}