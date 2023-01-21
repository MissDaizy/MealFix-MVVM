package com.diana_ukrainsky.mealfix.data.remote;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.diana_ukrainsky.mealfix.common.Constants;
import com.diana_ukrainsky.mealfix.data.model.recipe.Recipe;
import com.diana_ukrainsky.mealfix.data.model.recipe.RecipeList;

import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiManager {
    private final Callback_networkResponse callback_firstNetworkResponse;
    private final Callback_networkResponse callback_secondNetworkResponse;
    private final ApiService apiService;

    public ApiManager(
            Callback_networkResponse callback_firstNetworkResponse,
            Callback_networkResponse callback_secondNetworkResponse) {
        this.callback_firstNetworkResponse = callback_firstNetworkResponse;
        this.callback_secondNetworkResponse = callback_secondNetworkResponse;

        apiService = ApiService.getInstance();
    }

    public void start(int fromPage, int size) {
        // Perform some background task
//        retrieveRecipeListDataFromServer(fromPage, size);
    }




    public interface Callback_networkResponse<T> {
        void onSuccess(T object);
        void onError();
    }
}

