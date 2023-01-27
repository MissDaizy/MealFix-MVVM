package com.diana_ukrainsky.mealfix.data.model.recipe;

import com.diana_ukrainsky.mealfix.data.model.nutrition.Nutrition;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;
import java.util.Objects;

public class Recipe  {
    @SerializedName("id")
    private int recipeId;
    @SerializedName("name")
    private String recipeName;
    @SerializedName("description")
    private String recipeDescription;
    @SerializedName("prep_time_minutes")
    private  int cookTime;
    @SerializedName("thumbnail_url")
    private String recipeImage;
    @SerializedName("video_url")
    private String recipeVideo;
    @SerializedName("get_more_info_url")
    private String getMoreInfoUrl;
    @SerializedName("nutrition")
    private Nutrition nutrition;

    public Recipe() {
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeDescription() {
        return recipeDescription;
    }

    public void setRecipeDescription(String recipeDescription) {
        this.recipeDescription = recipeDescription;
    }

    public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }

    public String getRecipeVideo() {
        return recipeVideo;
    }

    public void setRecipeVideo(String recipeVideo) {
        this.recipeVideo = recipeVideo;
    }

    public String getGetMoreInfoUrl() {
        return getMoreInfoUrl;
    }

    public Recipe setGetMoreInfoUrl(String getMoreInfoUrl) {
        this.getMoreInfoUrl = getMoreInfoUrl;
        return this;
    }

    public Nutrition getNutrition() {
        return nutrition;
    }

    public void setNutrition(Nutrition nutrition) {
        this.nutrition = nutrition;
    }

    public void setAdditionalAttributes(Recipe recipe) {
        this.nutrition = recipe.nutrition;
        this.recipeDescription = recipeDescription;

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o instanceof Recipe) {
            return Objects.equals(recipeId, ((Recipe) o).recipeId);
        }
        return false;
    }


    public static class SortByTitle implements Comparator<Recipe> {
        // Used for sorting title
        public int compare(Recipe r1, Recipe r2) {
            return r1.getRecipeName().compareTo(r2.getRecipeName());
        }
    }
    public static class SortByCookTime implements Comparator<Recipe> {
        // Used for sorting in ascending order of
        // roll number
        public int compare(Recipe r1, Recipe r2) {
            return r1.getCookTime() - r2.getCookTime();
        }
    }
}