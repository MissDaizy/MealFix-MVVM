package com.diana_ukrainsky.mealfix.data.remote;

import com.diana_ukrainsky.mealfix.data.model.recipe.Recipe;
import com.diana_ukrainsky.mealfix.data.model.recipe.RecipeList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonApiRecipe {
    @GET("recipes/list")
    Call<RecipeList> getAllRecipes(
            @Query("rapidapi-key")String apiKey,
            @Query("from")int fromPage,
            @Query("size") int size
            );

    @GET("recipes/get-more-info")
    Call<Recipe> getRecipeDetails(
            @Query("rapidapi-key")String apiKey,
            @Query("id")int id
    );
}
