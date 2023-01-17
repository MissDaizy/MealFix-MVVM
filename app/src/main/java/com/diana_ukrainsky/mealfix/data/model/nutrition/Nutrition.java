package com.diana_ukrainsky.mealfix.data.model.nutrition;

import com.google.gson.annotations.SerializedName;

public class Nutrition {
    @SerializedName("sugar")
    private int sugar;
    @SerializedName("carbohydrates")
    private int carbohydrates;
    @SerializedName("fiber")
    private int fiber;
    @SerializedName("updated_at")
    private String updated_at;
    @SerializedName("protein")
    private int protein;
    @SerializedName("fat")
    private int fat;
    @SerializedName("calories")
    private int calories;

    public Nutrition() {
    }

    public int getSugar() {
        return sugar;
    }

    public void setSugar(int sugar) {
        this.sugar = sugar;
    }

    public int getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(int carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public int getFiber() {
        return fiber;
    }

    public void setFiber(int fiber) {
        this.fiber = fiber;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }
}
