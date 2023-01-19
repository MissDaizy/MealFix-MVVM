package com.diana_ukrainsky.mealfix.data.remote;

import com.diana_ukrainsky.mealfix.data.model.recipe.Recipe;
import com.diana_ukrainsky.mealfix.data.model.recipe.RecipeList;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonApiRecipe {
    // The Recipe list
    @GET("recipes/list")
    Observable<RecipeList> getAllRecipes(
            @Query("from") int fromPage,
            @Query("size") int size
    );

    // The Recipe details of every item in the list by id
    @GET("recipes/get-more-info/{recipeUrlId}")
    Observable<Recipe> getRecipeDetails(
            @Path("recipeUrlId") String recipeUrlId
    );
}
