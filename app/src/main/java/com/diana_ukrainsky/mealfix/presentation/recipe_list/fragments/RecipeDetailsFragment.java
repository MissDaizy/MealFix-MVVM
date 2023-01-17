package com.diana_ukrainsky.mealfix.presentation.recipe_list.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.diana_ukrainsky.mealfix.data.model.recipe.Recipe;
import com.diana_ukrainsky.mealfix.databinding.FragmentRecipeDetailsBinding;
import com.diana_ukrainsky.mealfix.presentation.recipe_list.CustomObjectListener;
import com.diana_ukrainsky.mealfix.presentation.recipe_list.ItemListener;
import com.diana_ukrainsky.mealfix.presentation.recipe_list.RecipeAdapter;
import com.diana_ukrainsky.mealfix.presentation.recipe_list.RecipeListViewModel;

import java.util.ArrayList;


public class RecipeDetailsFragment extends Fragment implements LifecycleOwner {
    private RecipeListViewModel recipeListViewModel;
    private FragmentRecipeDetailsBinding fragmentRecipeDetailsBinding;

    private RecipeAdapter recipeAdapter;

    private CustomObjectListener customObjectListener;


    int position;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    public static RecipeDetailsFragment newInstance(String param1, String param2) {
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentRecipeDetailsBinding = FragmentRecipeDetailsBinding.inflate(inflater, container, false);
        View view = fragmentRecipeDetailsBinding.getRoot();

        position = getArguments().getInt("position", 0);

        setViewModel();
        setViews(position);

        setListeners();
//
//        setRecipeListUI();

        return view;
    }

    private void setListeners() {
        ItemListener itemListener = new ItemListener() {
            @Override
            public void onItemUpdated() {
                setRecipeDetailsUI();

            }
        };
        recipeListViewModel.setItemLisener(itemListener);
    }

    private void setViews(int position) {
        Recipe recipe = recipeListViewModel.getRecipeListData().getValue().get(position);
        setImage(recipe.getRecipeImage());
        fragmentRecipeDetailsBinding.recipeListItemTXTTitle.setText(recipe.getRecipeName());
        fragmentRecipeDetailsBinding.fragmentRecipeDetailsTXTCookingTime.setText(String.valueOf(recipe.getCookTime()));
//        fragmentRecipeDetailsBinding.fragmentRecipeDetailsTXTCookingTime.setText(recipe.getGetMoreInfoUrl().substring(recipe.getGetMoreInfoUrl().lastIndexOf("/") + 1));
    }

    private void setImage(String recipeImage) {
        Glide.with(getContext())
                .load(recipeImage)
                .override(1000,1000)
                .into(fragmentRecipeDetailsBinding.recipeListItemIMGRecipeImage);
    }

    private void setViewModel() {
        recipeListViewModel = new ViewModelProvider(getActivity()).get(RecipeListViewModel.class);
        recipeListViewModel.getRecipeListData().observe(getViewLifecycleOwner(), recipeListUpdateObserver);

    }
    Observer<ArrayList<Recipe>> recipeListUpdateObserver = new Observer<ArrayList<Recipe>>() {
        @Override
        public void onChanged(ArrayList<Recipe> recipeArrayList) {
            // Update the UI with the new data

        }
    };

    private void setRecipeDetailsUI() {
        Recipe recipe = recipeListViewModel.getRecipeListData().getValue().get(position);
        //fragmentRecipeDetailsBinding.fragmentRecipeDetailsTXTCookingTime.setText(recipe.getGetMoreInfoUrl().substring(recipe.getGetMoreInfoUrl().lastIndexOf("/") + 1));
        fragmentRecipeDetailsBinding.fragmentRecipeDetailsTXTDescription.setText(recipe.getRecipeDescription());



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CustomObjectListener) {
            customObjectListener = (CustomObjectListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        customObjectListener = null;
    }




}
