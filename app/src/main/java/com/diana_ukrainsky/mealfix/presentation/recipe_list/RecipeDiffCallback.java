package com.diana_ukrainsky.mealfix.presentation.recipe_list;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.diana_ukrainsky.mealfix.data.model.recipe.Recipe;

import java.util.List;

public class RecipeDiffCallback extends  DiffUtil.Callback {

    private final List<Recipe> mOldRecipeList;
    private final List<Recipe> mNewRecipeList;

    public RecipeDiffCallback(List<Recipe> oldRecipeList, List<Recipe> newRecipeList) {
        this.mOldRecipeList = oldRecipeList;
        this.mNewRecipeList = newRecipeList;
    }

    @Override
    public int getOldListSize() {
        return mOldRecipeList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewRecipeList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldRecipeList.get(oldItemPosition).getRecipeId() == mNewRecipeList.get(
                newItemPosition).getRecipeId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Recipe oldRecipe = mOldRecipeList.get(oldItemPosition);
        final Recipe newRecipe = mNewRecipeList.get(newItemPosition);

        return oldRecipe.getRecipeName().equals(newRecipe.getRecipeName());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
