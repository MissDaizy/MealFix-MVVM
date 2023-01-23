package com.diana_ukrainsky.mealfix.ui.home;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.diana_ukrainsky.mealfix.R;
import com.diana_ukrainsky.mealfix.data.model.recipe.Recipe;
import com.diana_ukrainsky.mealfix.databinding.ActivityHomeBinding;
import com.diana_ukrainsky.mealfix.ui.callback.CustomItemClickListener;
import com.diana_ukrainsky.mealfix.ui.recipe_list.RecipeListEvent;
import com.diana_ukrainsky.mealfix.ui.recipe_list.RecipeListViewModel;
import com.diana_ukrainsky.mealfix.ui.recipe.RecipeDetailsFragment;
import com.diana_ukrainsky.mealfix.ui.recipe_list.fragment.RecipeListFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeActivity extends AppCompatActivity implements CustomItemClickListener {
    private RecipeListViewModel recipeListViewModel;

    private ActivityHomeBinding activityHomeBinding;

    private FragmentManager fragmentManager;
    private RecipeDetailsFragment recipeDetailsFragment;

    private CustomItemClickListener customItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        activityHomeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = activityHomeBinding.getRoot();
        setContentView(view);

        recipeListViewModel = new ViewModelProvider(this).get(RecipeListViewModel.class);

        setFragmentManager(savedInstanceState);

    }

    private void setFragmentManager(Bundle savedInstanceState) {
        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            RecipeListFragment recipeListFragment = new RecipeListFragment(this);
            fragmentManager.beginTransaction()
                    .add(R.id.activityHome_FL_frameLayout, recipeListFragment)
                    .commit();
        }
    }

    @Override
    public void onClick(Object object) {

        recipeListViewModel
                .onEventRecipeList(
                        RecipeListEvent.ListItemClicked,
                        object
                );
        recipeDetailsFragment = new RecipeDetailsFragment();


        fragmentManager.beginTransaction()
                .replace(R.id.activityHome_FL_frameLayout, recipeDetailsFragment)
                .addToBackStack(null)
                .commit();
    }
}