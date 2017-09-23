package com.anhhoang.picrust.data.network;

import com.anhhoang.picrust.data.models.RecipeRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by anh.hoang on 9/23/17.
 */

public class PiCrustApiService {

    private static final String URL = "https://d17h27t6h515a5.cloudfront.net/";
    private static PiCrustApiService INSTANCE;
    private Retrofit retrofit;
    private PiCrustApi piCrustApi;

    private PiCrustApiService() {
        retrofit = new Retrofit
                .Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        piCrustApi = retrofit.create(PiCrustApi.class);
    }

    public static PiCrustApiService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PiCrustApiService();
        }

        return INSTANCE;
    }

    public Call<List<RecipeRequest>> getRecipes() {
        return piCrustApi.getRecipes();
    }

    private interface PiCrustApi {
        @GET("topher/2017/May/59121517_baking/baking.json")
        Call<List<RecipeRequest>> getRecipes();
    }
}
