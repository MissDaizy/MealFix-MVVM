package com.diana_ukrainsky.repository;

import com.diana_ukrainsky.mealfix.data.model.recipe.Recipe;
import com.diana_ukrainsky.mealfix.data.model.recipe.RecipeList;
import com.diana_ukrainsky.mealfix.data.remote.ApiService;
import com.diana_ukrainsky.mealfix.data.remote.JsonApiRecipe;
import com.diana_ukrainsky.mealfix.ui.recipe_list.pagination.PaginationManager;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class Repository {
    private JsonApiRecipe jsonApiRecipe;

    @Inject
    public Repository(JsonApiRecipe jsonApiRecipe) {
        this.jsonApiRecipe = jsonApiRecipe;
    }
    public Observable<RecipeList> getRecipes(){
        return jsonApiRecipe.getAllRecipes(PaginationManager.getInstance().getCurrentPage(),PaginationManager.getInstance().getMaxResultsInPage());
    }

}
