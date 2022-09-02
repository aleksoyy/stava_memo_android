package com.stavamemo.examples.stava.API;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper{
    private static final String baseUrl = "https://portowebyosua.000webhostapp.com/stavamemo/";
    private static Retrofit retrofit;

    public static Retrofit conRetrofit(){
        if (retrofit == null){

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
