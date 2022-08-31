package com.stavamemo.examples.stava.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.stavamemo.examples.stava.API.APIRequestData;
import com.stavamemo.examples.stava.API.RetrofitHelper;
import com.stavamemo.examples.stava.Activity.MainActivity;
import com.stavamemo.examples.stava.Activity.MemoPage;
import com.stavamemo.examples.stava.Model.MemoModel;
import com.stavamemo.examples.stava.Model.ResponseModel;
import com.stavamemo.examples.stava.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterData extends RecyclerView.Adapter<AdapterData.HolderData> {
    private Context context;
    private List<MemoModel> memoList;
    private int mid;
    private int userid;

    public AdapterData(Context context, List<MemoModel> memoList) {
        this.context = context;
        this.memoList = memoList;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_memo, parent, false);
        HolderData holderData = new HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(HolderData holder, int position) {
        MemoModel mm = memoList.get(position);

        holder.memoId.setText(String.valueOf(mm.getMid()));
        holder.uid.setText(String.valueOf(mm.getUid()));
        holder.memoTitle.setText(mm.getMtitle());
        holder.memoDate.setText(mm.getMdate());
        holder.memoContains.setText(mm.getMcont());

    }
    @Override
    public int getItemCount() {
        return memoList == null ? 0 : memoList.size();
    }

    public class HolderData extends RecyclerView.ViewHolder{
        TextView uid, memoId, memoTitle, memoContains, memoDate;

        public HolderData( View itemView) {
            super(itemView);

            uid = itemView.findViewById(R.id.uid);
            memoId = itemView.findViewById(R.id.memoId);
            memoTitle = itemView.findViewById(R.id.memoTitle);
            memoDate = itemView.findViewById(R.id.memoDate);
            memoContains = itemView.findViewById(R.id.memoContain);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent oldMemoIntent = new Intent(view.getContext(), MemoPage.class);
                    oldMemoIntent.putExtra("mid", memoId.getText().toString());
                    oldMemoIntent.putExtra("uid", uid.getText().toString());
                    view.getContext().startActivity(oldMemoIntent);
                }
            });

            itemView.setOnLongClickListener(view -> {
                AlertDialog.Builder dialogOpt = new AlertDialog.Builder(context);
                dialogOpt.setMessage("Delete This Memo ?");
                dialogOpt.setCancelable(true);

                dialogOpt.setPositiveButton("Delete", (dialogInterface, i) -> {
                    mid = Integer.parseInt(memoId.getText().toString());
                    deleteMemoData();
                    dialogInterface.dismiss();
                    ((MainActivity) context).retrieveData();
                });

                dialogOpt.show();
                return false;
            });

        }

        private void deleteMemoData(){
            APIRequestData requestData = RetrofitHelper.conRetrofit().create(APIRequestData.class);
            Call<ResponseModel> delMemo = requestData.deleteMemo(mid);
            delMemo.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int code = response.body().getCode();
                    String message = response.body().getMessage();

                    Toast.makeText(context, "Delete Success"+mid , Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(context, "Delete Failed : "+t.getMessage() , Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
