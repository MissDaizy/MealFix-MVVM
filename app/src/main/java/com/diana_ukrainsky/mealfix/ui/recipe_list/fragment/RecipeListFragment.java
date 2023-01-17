package com.diana_ukrainsky.mealfix.ui.recipe_list.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.diana_ukrainsky.mealfix.common.Constants;
import com.diana_ukrainsky.mealfix.data.model.recipe.Recipe;
import com.diana_ukrainsky.mealfix.data.model.recipe.RecipeList;
import com.diana_ukrainsky.mealfix.databinding.FragmentRecipeListBinding;
import com.diana_ukrainsky.mealfix.ui.callback.CustomItemClickListener;
import com.diana_ukrainsky.mealfix.ui.recipe_list.pagination.PaginationScrollListener;
import com.diana_ukrainsky.mealfix.ui.recipe_list.adapter.RecipeAdapter;
import com.diana_ukrainsky.mealfix.ui.recipe_list.RecipeListEvent;
import com.diana_ukrainsky.mealfix.ui.recipe_list.RecipeListViewModel;

import java.util.ArrayList;

public class RecipeListFragment extends Fragment implements LifecycleOwner {

    private RecipeListViewModel recipeListViewModel;

    private FragmentRecipeListBinding fragmentRecipeListBinding;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;

    private CustomItemClickListener customItemClickListener;

    private ProgressBar progressBar;

    public RecipeListFragment() {
        // Required empty public constructor
    }

    public RecipeListFragment(CustomItemClickListener customItemClickListener) {
        this.customItemClickListener = customItemClickListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentRecipeListBinding = FragmentRecipeListBinding.inflate(inflater, container, false);
        View view = fragmentRecipeListBinding.getRoot();

        setViewModel();
        setViews();
        setRecyclerView();
        setAdapter();
        setRecipeListUI();

        return view;
    }

    private void setViews() {
        progressBar = fragmentRecipeListBinding.fragmentRecipeListPBProgressBar;
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void setViewModel() {
        recipeListViewModel = new ViewModelProvider(getActivity()).get(RecipeListViewModel.class);
        recipeListViewModel.getRecipeListData().observe(this.getViewLifecycleOwner(), recipeListUpdateObserver);
    }

    private void setRecyclerView() {
        recyclerView = fragmentRecipeListBinding.fragmentRecipeListRVRecyclerView;

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    Observer<ArrayList<Recipe>> recipeListUpdateObserver = new Observer<ArrayList<Recipe>>() {
        @Override
        public void onChanged(ArrayList<Recipe> recipeArrayList) {
            recipeAdapter.notifyDataSetChanged();
        }
    };

    private void setRecipeListUI() {
        loadFirstPage();
        loadMoreItems();
    }

    private void setAdapter() {
        recipeAdapter = new RecipeAdapter(getContext(), recipeListViewModel.getRecipeListData().getValue(), customItemClickListener);
        recyclerView.setAdapter(recipeAdapter);
    }


    private void loadMoreItems() {
        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                progressBar.setVisibility(View.VISIBLE);
                recipeListViewModel.onEventRecipeList(RecipeListEvent.LoadMoreItems, null);
                loadNextPage();
            }

            @Override
            public boolean isLastPage() {
                return recipeListViewModel.isLastPage();
            }

            @Override
            public boolean isLoading() {
                return recipeListViewModel.isLoading();
            }
        });
    }

    private void loadNextPage() {
        recipeListViewModel.populateList(object -> {
                    if (object != null) {
                        RecipeList recipeList = ((RecipeList) object);
                        ArrayList<Recipe> recipes = (ArrayList<Recipe>) recipeList.getRecipeList();
                        recipeListViewModel.onEventRecipeList(RecipeListEvent.LoadNextPage, recipeList.getCount());
                        recipeAdapter.addAll(recipes);
                        recipeAdapter.notifyDataSetChanged();

                    } else {
                        //AlertUtils.showToast (getApplicationContext (), movieList.getError ());
                        progressBar.setVisibility(View.GONE);
                    }
                },
                object -> {
                    if (object != null) {
                        /*  In case i'll want in the future fetch Recipe details right away
                        after the recipeList
                         */
                    }

                });
    }


    private void loadFirstPage() {
        recipeListViewModel.populateList(object -> {
                    if (object != null) {
                        RecipeList recipeList = ((RecipeList) object);
                        ArrayList<Recipe> recipes = (ArrayList<Recipe>) recipeList.getRecipeList();
                        recipeListViewModel.onEventRecipeList(RecipeListEvent.LoadFirstPage, recipeList.getCount());
                        progressBar.setVisibility(View.GONE);
                        recipeAdapter.updateRecipeListItems(recipes);


                    } else {
                        progressBar.setVisibility(View.GONE);
                    }
                },
                object -> {
                    if (object != null) {
                        /*  In case i'll want in the future fetch Recipe details right away
                        after the recipeList
                         */

                    }

                });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CustomItemClickListener) {
            customItemClickListener = (CustomItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        customItemClickListener = null;
    }

}