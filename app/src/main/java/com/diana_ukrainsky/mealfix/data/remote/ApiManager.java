package com.diana_ukrainsky.mealfix.data.remote;

import android.util.Log;

import com.diana_ukrainsky.mealfix.common.Constants;
import com.diana_ukrainsky.mealfix.data.model.recipe.Recipe;
import com.diana_ukrainsky.mealfix.data.model.recipe.RecipeList;

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
        retrieveRecipeListDataFromServer(fromPage, size);
    }

    private void retrieveRecipeListDataFromServer(int fromPage, int size) {
        Call<RecipeList> call = apiService.getJsonApiRecipe().getAllRecipes(
                fromPage,
                size
        );

        call.enqueue(new Callback<RecipeList>() {
            @Override
            public void onResponse(Call<RecipeList> call, Response<RecipeList> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    callback_firstNetworkResponse.onSuccess(response.body());
                } else {
                    Log.d(Constants.LOG, response.errorBody().toString());

                    callback_firstNetworkResponse.onError();
                }

            }

            @Override
            public void onFailure(Call<RecipeList> call, Throwable t) {
                callback_firstNetworkResponse.onError();
                Log.d(Constants.LOG, "Throwable t message: " + t.getMessage());
            }
        });
    }

    public void retrieveRecipeDetailsDataFromServer(String recipeUrl, int id, Callback_networkResponse callback_networkResponse) {
        recipeUrl = recipeUrl.substring(recipeUrl.lastIndexOf("/") + 1);
        Call<Recipe> call = apiService.getJsonApiRecipe().getRecipeDetails(
                recipeUrl
        );

        call.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    callback_networkResponse.onSuccess(response.body());
                } else {
                    callback_secondNetworkResponse.onError();
                }
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                callback_firstNetworkResponse.onError();
            }
        });
    }


    public interface Callback_networkResponse<T> {
        void onSuccess(T object);
        void onError();
    }
}

