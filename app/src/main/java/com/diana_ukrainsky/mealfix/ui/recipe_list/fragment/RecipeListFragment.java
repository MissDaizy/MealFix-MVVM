package com.diana_ukrainsky.mealfix.ui.recipe_list.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.diana_ukrainsky.mealfix.common.Constants;
import com.diana_ukrainsky.mealfix.data.model.recipe.Recipe;
import com.diana_ukrainsky.mealfix.data.model.recipe.RecipeList;
import com.diana_ukrainsky.mealfix.databinding.FragmentRecipeListBinding;
import com.diana_ukrainsky.mealfix.databinding.ItemProgressBinding;
import com.diana_ukrainsky.mealfix.ui.callback.CustomItemClickListener;
import com.diana_ukrainsky.mealfix.ui.recipe_list.FilterType;
import com.diana_ukrainsky.mealfix.ui.recipe_list.pagination.PaginationManager;
import com.diana_ukrainsky.mealfix.ui.recipe_list.pagination.PaginationScrollListener;
import com.diana_ukrainsky.mealfix.ui.recipe_list.adapter.RecipeAdapter;
import com.diana_ukrainsky.mealfix.ui.recipe_list.RecipeListEvent;
import com.diana_ukrainsky.mealfix.ui.recipe_list.RecipeListViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RecipeListFragment extends Fragment implements LifecycleOwner {

    private RecipeListViewModel recipeListViewModel;
    private FragmentRecipeListBinding fragmentRecipeListBinding;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;

    private CustomItemClickListener customItemClickListener;

    // Progress Bar for loading items  in the first page, not by scrolling
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
        recipeListViewModel = new ViewModelProvider(requireActivity()).get(RecipeListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentRecipeListBinding = FragmentRecipeListBinding.inflate(inflater, container, false);
        View view = fragmentRecipeListBinding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewModel();

        setViews();
        setListeners();
        setRecyclerView();
        setAdapter();
        setRecipeListUI();
    }

    private void setListeners() {
        // Can be replaced with a SearchView
        fragmentRecipeListBinding.fragmentRecipeListEDTSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                recipeListViewModel.searchRecipes(editable.toString().toLowerCase(Locale.ROOT));

            }
        });
        setFilterButtonsListeners();
    }

    private void setFilterButtonsListeners() {
        fragmentRecipeListBinding.fragmentRecipeListBTNAllFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeListViewModel.onEventRecipeList(RecipeListEvent.FilterList, FilterType.ALL);
            }
        });
        fragmentRecipeListBinding.fragmentRecipeListBTNAscCookTimeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeListViewModel.onEventRecipeList(RecipeListEvent.FilterList, FilterType.ASC_COOK_TIME);

            }
        });

        fragmentRecipeListBinding.fragmentRecipeListBTNDescCookTimeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeListViewModel.onEventRecipeList(RecipeListEvent.FilterList, FilterType.DESC_COOK_TIME);
            }
        });
        fragmentRecipeListBinding.fragmentRecipeListBTNTitleFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeListViewModel.onEventRecipeList(RecipeListEvent.FilterList, FilterType.TITLE);
            }
        });
    }

    private void setViews() {
        progressBar = fragmentRecipeListBinding.fragmentRecipeListPBProgressBar;
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void setViewModel() {
        // Observe Recipe List Live Data
        recipeListViewModel.getRecipeListData().observe(this.getViewLifecycleOwner(), recipeListUpdateObserver);
        // Observe Loading Live Data
        recipeListViewModel.getLoading().observe(this.getViewLifecycleOwner(), loadingObserver);
        // Observe Filtered Recipe Live Data
        recipeListViewModel.getFilteredLiveData().observe(this.getViewLifecycleOwner(), filteredRecipeListUpdateObserver);

//        // Another way of implementation of observing the mutable list
//        recipeListViewModel.getRecipeListData().observe(this.getViewLifecycleOwner(), recipes -> {
//            recipeAdapter.updateRecipeListItems(recipes);
//        });
    }

    private void setRecyclerView() {
        recyclerView = fragmentRecipeListBinding.fragmentRecipeListRVRecyclerView;

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    Observer<List<Recipe>> recipeListUpdateObserver = new Observer<List<Recipe>>() {
        @Override
        public void onChanged(List<Recipe> recipeList) {
            recipeAdapter.updateRecipeListItems(recipeList);
        }
    };

    Observer<Boolean> loadingObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isLoading) {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        }
    };

    Observer<List<Recipe>> filteredRecipeListUpdateObserver = new Observer<List<Recipe>>() {
        @Override
        public void onChanged(List<Recipe> recipes) {
            recipeAdapter.updateRecipeListItems(recipes);

        }
    };

    private void setRecipeListUI() {
        loadFirstPage();
        // loadMoreItems();
    }

    private void setAdapter() {
        recipeAdapter = new RecipeAdapter(getContext(), customItemClickListener);
        recyclerView.setAdapter(recipeAdapter);
    }


//    private void loadMoreItems() {
//        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
//            @Override
//            protected void loadMoreItems() {
//                progressBar.setVisibility(View.VISIBLE);
//                recipeListViewModel.onEventRecipeList(RecipeListEvent.LoadMoreItems, null);
//                loadNextPage();
//            }
//
//            @Override
//            public boolean isLastPage() {
//                return recipeListViewModel.isLastPage();
//            }
//
//            @Override
//            public boolean isLoading() {
//                return recipeListViewModel.isLoading();
//            }
//        });
//    }

//
//    private void loadNextPage() {
//        recipeListViewModel.populateList(object -> {
//                    if (object != null) {
//                        RecipeList recipeList = ((RecipeList) object);
//                        ArrayList<Recipe> recipes = (ArrayList<Recipe>) recipeList.getRecipeList();
//                        recipeListViewModel.onEventRecipeList(RecipeListEvent.LoadNextPage, recipeList.getCount());
//                        recipeAdapter.addAll(recipes);
//                        recipeAdapter.notifyDataSetChanged();
//
//                    } else {
//                        //AlertUtils.showToast (getApplicationContext (), movieList.getError ());
//                        progressBar.setVisibility(View.GONE);
//                    }
//                },
//                object -> {
//                    if (object != null) {
//                        /*  In case i'll want in the future fetch Recipe details right away
//                        after the recipeList
//                         */
//                    }
//
//                });
//    }


    private void loadFirstPage() {
        // TODO: Hide and show loading spinner
        recipeListViewModel.getRecipes();
//        recipeListViewModel.populateList(object -> {
//                    if (object != null) {
//                        RecipeList recipeList = ((RecipeList) object);
//                        ArrayList<Recipe> recipes = (ArrayList<Recipe>) recipeList.getRecipeList();
//                        recipeListViewModel.onEventRecipeList(RecipeListEvent.LoadFirstPage, recipeList.getCount());
//                        progressBar.setVisibility(View.GONE);
//                        recipeAdapter.updateRecipeListItems(recipes);
//
//
//                    } else {
//                        progressBar.setVisibility(View.GONE);
//                    }
//                },
//                object -> {
//                    if (object != null) {
//                        /*  In case i'll want in the future fetch Recipe details right away
//                        after the recipeList
//                         */
//
//                    }
//
//                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Destroy composite when fragment is destroyed.
        recipeListViewModel.disposeComposite();
    }
}