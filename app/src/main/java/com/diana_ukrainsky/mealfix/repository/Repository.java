package com.diana_ukrainsky.mealfix.repository;

import com.diana_ukrainsky.mealfix.data.model.recipe.Recipe;
import com.diana_ukrainsky.mealfix.data.model.recipe.RecipeList;
import com.diana_ukrainsky.mealfix.data.remote.JsonApiRecipe;
import com.diana_ukrainsky.mealfix.ui.recipe_list.pagination.PaginationManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Observable;

@Singleton
public class Repository {
    private JsonApiRecipe jsonApiRecipe;

    @Inject
    public Repository(JsonApiRecipe jsonApiRecipe) {
        this.jsonApiRecipe = jsonApiRecipe;
    }

    public Observable<RecipeList> getRecipes(int currentPage, int maxResultsInPage){
        return jsonApiRecipe.getAllRecipes(currentPage,PaginationManager.getInstance().getMaxResultsInPage());
    }
    public Observable<Recipe> getRecipeDetails(Recipe recipe){
        return jsonApiRecipe.getRecipeDetails(recipe.getGetMoreInfoUrl());
    }




}
