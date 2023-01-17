package com.diana_ukrainsky.mealfix.data.remote;

import com.diana_ukrainsky.mealfix.common.Constants;
import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    private static ApiService INSTANCE = null;
    private Retrofit retrofit;
    private JsonApiRecipe jsonApiRecipe;

    private ApiService() {
        initializeRetrofit();
        setJsonPlaceholders();
    }

    private void setJsonPlaceholders() {
        jsonApiRecipe = retrofit.create(JsonApiRecipe.class);
    }

    public static ApiService getInstance() {
        if (INSTANCE == null)
            INSTANCE = new ApiService();

        return INSTANCE;
    }

    private void initializeRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.MY_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public JsonApiRecipe getJsonApiRecipe() {
        return jsonApiRecipe;
    }

}
