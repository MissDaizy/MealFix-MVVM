package com.diana_ukrainsky.mealfix.data.model.recipe;

import com.google.gson.annotations.SerializedName;

public class Recipe {
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
    @SerializedName("num_servings")
    private String numServings;

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

    public String getNumServings() {
        return numServings;
    }

    public void setNumServings(String numServings) {
        this.numServings = numServings;
    }
}
