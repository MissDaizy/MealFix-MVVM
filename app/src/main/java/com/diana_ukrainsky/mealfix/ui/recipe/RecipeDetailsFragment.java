package com.diana_ukrainsky.mealfix.ui.recipe;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diana_ukrainsky.mealfix.data.model.recipe.Recipe;
import com.diana_ukrainsky.mealfix.databinding.FragmentRecipeDetailsBinding;
import com.diana_ukrainsky.mealfix.ui.callback.CustomItemClickListener;
import com.diana_ukrainsky.mealfix.ui.recipe_list.RecipeListViewModel;
import com.diana_ukrainsky.mealfix.ui.recipe_list.fragment.RecipeListFragment;
import com.diana_ukrainsky.mealfix.utils.ImageUtil;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RecipeDetailsFragment extends Fragment implements LifecycleOwner {
    private RecipeListViewModel recipeListViewModel;
    private FragmentRecipeDetailsBinding fragmentRecipeDetailsBinding;

    private CustomItemClickListener customItemClickListener;

    private final int RECIPE_IMAGE_HEIGHT = 1000;
    private final int RECIPE_IMAGE_WIDTH = 1000;

    int position;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentRecipeDetailsBinding = FragmentRecipeDetailsBinding.inflate(inflater, container, false);

        recipeListViewModel = new ViewModelProvider(requireActivity()).get(RecipeListViewModel.class);

        setViewModel();

        Recipe recipe=recipeListViewModel.getSelectedRecipe().getValue();
        setViews(recipe);
        View view = fragmentRecipeDetailsBinding.getRoot();

        return view;
    }

    private void setViews(Recipe recipe) {
        setImage(recipe.getRecipeImage());
        fragmentRecipeDetailsBinding.recipeListItemTXTTitle.setText(recipe.getRecipeName());
        fragmentRecipeDetailsBinding.fragmentRecipeDetailsTXTCookingTime.setText(String.valueOf(recipe.getCookTime()));
    }

    private void setImage(String recipeImage) {
        ImageUtil.setImageUI(getContext(),
                recipeImage,
                fragmentRecipeDetailsBinding.
                        recipeListItemIMGRecipeImage,
                RECIPE_IMAGE_WIDTH,
                RECIPE_IMAGE_HEIGHT);
    }

    private void setViewModel() {
        recipeListViewModel.getSelectedRecipe().observe(getViewLifecycleOwner(), recipeUpdateObserver);
        recipeListViewModel.getSelectedRecipe().observe(getViewLifecycleOwner(), recipeObserver);
//        recipeListViewModel.getIsSelectedRecipeUpdated().observe(getViewLifecycleOwner(), isRecipeUpdatedObserver);

    }

    Observer<Recipe> recipeUpdateObserver = new Observer<Recipe>() {
        @Override
        public void onChanged(Recipe recipe) {
            if(recipe.getNutrition()!=null) {
                setRecipeDetailsUI(recipe);
            }
        }
    };

    Observer<Recipe> recipeObserver=new Observer<Recipe>() {
        @Override
        public void onChanged(Recipe recipe) {
            if(recipe!=null)
                setViews(recipe);
        }
    };

    private void setRecipeDetailsUI(Recipe recipe) {
        fragmentRecipeDetailsBinding.fragmentRecipeDetailsTXTDescription.setText(String.valueOf(recipe.getRecipeDescription()));
        fragmentRecipeDetailsBinding.fragmentRecipeDetailsTXTNutritionSugar.setText(String.valueOf(recipe.getNutrition().getSugar()));
        fragmentRecipeDetailsBinding.fragmentRecipeDetailsTXTCarbohydrates.setText(String.valueOf(recipe.getNutrition().getCarbohydrates()));
        fragmentRecipeDetailsBinding.fragmentRecipeDetailsTXTFiber.setText(String.valueOf(recipe.getNutrition().getFiber()));
        fragmentRecipeDetailsBinding.fragmentRecipeDetailsTXTProtein.setText(String.valueOf(recipe.getNutrition().getProtein()));
        fragmentRecipeDetailsBinding.fragmentRecipeDetailsTXTFat.setText(String.valueOf(recipe.getNutrition().getFat()));
        fragmentRecipeDetailsBinding.fragmentRecipeDetailsTXTCalories.setText(String.valueOf(recipe.getNutrition().getCalories()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CustomItemClickListener) {
            customItemClickListener = (CustomItemClickListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnListItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        customItemClickListener = null;
    }


}
