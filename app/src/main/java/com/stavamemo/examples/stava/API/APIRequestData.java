package com.stavamemo.examples.stava.API;
import com.stavamemo.examples.stava.Model.ResponseModel;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIRequestData {

    @FormUrlEncoded
    @POST("fetchmemo.php")
    Call<ResponseModel> fetchDataModel(
            @Field("uid") int uid
    );

    @FormUrlEncoded
    @POST("deletememo.php")
    Call<ResponseModel> deleteMemo(
            @Field("mid") int mid
    );

    @FormUrlEncoded
    @POST("getmemospesific.php")
    Call<ResponseModel> getSpecMemo(
            @Field("mid") int mid
    );

    @FormUrlEncoded
    @POST("updatememo.php")
    Call<ResponseModel> updatMemoData(
            @Field("mtitle") String mtitle,
            @Field("mdate") String mdate,
            @Field("mcont") String mcont
    );

    @FormUrlEncoded
    @POST("addmemo.php")
    Call<ResponseModel> uploadDataModel(
            @Field("mtitle") String mtitle,
            @Field("mdate") String mdate,
            @Field("mcont") String mcont,
            @Field("uid") int uid
    );

    @FormUrlEncoded
    @POST("authlogin.php")
    Call<ResponseModel> authLogin(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("authregis.php")
    Call<ResponseModel> authregis(
            @Field("username") String username,
            @Field("name") String name,
            @Field("password") String password,
            @Field("email") String email
    );
}
