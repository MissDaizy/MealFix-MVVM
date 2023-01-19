package com.diana_ukrainsky.mealfix.di;


import com.diana_ukrainsky.mealfix.common.Constants;
import com.diana_ukrainsky.mealfix.data.remote.JsonApiRecipe;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    @Provides
    @Singleton
    public static JsonApiRecipe provideRecipeApiService(){

        return  new Retrofit.Builder()
                .baseUrl(Constants.MY_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(JsonApiRecipe.class);
    }
}
