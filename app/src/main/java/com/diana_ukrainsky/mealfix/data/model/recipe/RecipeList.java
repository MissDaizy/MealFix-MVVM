package com.diana_ukrainsky.mealfix.data.model.recipe;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeList {
    @SerializedName("count")
    private int count;
    @SerializedName("results")
    private List<Recipe> recipeList;

    public RecipeList() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }
}
