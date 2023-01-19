package com.diana_ukrainsky.mealfix.ui.recipe_list;

import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diana_ukrainsky.mealfix.common.Constants;
import com.diana_ukrainsky.mealfix.data.model.recipe.Recipe;
import com.diana_ukrainsky.mealfix.data.model.recipe.RecipeList;
import com.diana_ukrainsky.mealfix.data.remote.ApiManager;
import com.diana_ukrainsky.mealfix.data.callback.callbacks.Callback_retrofitResponse;
import com.diana_ukrainsky.mealfix.data.remote.ApiService;
import com.diana_ukrainsky.mealfix.ui.callback.CustomItemUpdateListener;
import com.diana_ukrainsky.mealfix.ui.recipe_list.pagination.PaginationManager;
import com.diana_ukrainsky.repository.Repository;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class RecipeListViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Recipe>> recipeLiveData;
    ArrayList<Recipe> recipeArrayList;
    Recipe currentClickedRecipe;
    int clickedRecipePosition;

    private ApiService apiService;
    private Repository repository;

    ApiManager apiManager;
    private PaginationManager paginationManager;

    private CustomItemUpdateListener customItemUpdateListener;

    @Inject
    public RecipeListViewModel(Repository repository) {
        this.repository=repository;
        recipeLiveData = new MutableLiveData<>();

        init();
    }

    public MutableLiveData<ArrayList<Recipe>> getRecipeMutableLiveData() {
        return recipeLiveData;
    }

    public void init() {
        apiService = ApiService.getInstance();
        paginationManager = PaginationManager.getInstance();
        recipeArrayList = new ArrayList<>();
        recipeLiveData.setValue(recipeArrayList);
    }

    public void populateList(
            Callback_retrofitResponse callback_firstRetrofitResponse,
            Callback_retrofitResponse callback_secondRetrofitResponse
    ) {

        apiManager = new ApiManager(new ApiManager.Callback_networkResponse() {
            @Override
            public void onSuccess(Object object) {
                RecipeList recipeList = (RecipeList) object;
                recipeArrayList.addAll(recipeList.getRecipeList());
                recipeLiveData.setValue(recipeArrayList);
                callback_firstRetrofitResponse.onResult(recipeList);
            }

            @Override
            public void onError() {
                Log.d(Constants.LOG, "Error: ");
            }
        }, new ApiManager.Callback_networkResponse() {
            @Override
            public void onSuccess(Object object) {
                Recipe recipe = (Recipe) object;
                // Return the recipe
                callback_secondRetrofitResponse.onResult(recipe);
            }

            @Override
            public void onError() {
                Log.d(Constants.LOG, "Error");

            }
        });
        apiManager.start(
                paginationManager.getCurrentPage(),
                paginationManager.getMaxResultsInPage()
        );

    }

    public MutableLiveData<ArrayList<Recipe>> getRecipeListData() {
        if (recipeLiveData == null) {
            recipeLiveData = new MutableLiveData<>();
        }

        return recipeLiveData;
    }

    public void getRecipes(int fromPage, int size) {
                repository.getRecipes()
                .subscribeOn(Schedulers.io())
                .map(new Function<RecipeList, RecipeList>() {
                    @Override
                    public RecipeList apply(RecipeList recipeList) throws Throwable {
                        return recipeList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> recipeLiveData.setValue(new ArrayList<>(result.getRecipeList())),
                        error -> Log.e(Constants.LOG, "getRecipes: " + error.getMessage())
                );
    }


    public void onEventRecipeList(RecipeListEvent event, Object object) {
        switch (event) {
            case ListItemClicked: {
                clickedRecipePosition = ((int) object);
                currentClickedRecipe = recipeArrayList.get(clickedRecipePosition);

                apiManager.retrieveRecipeDetailsDataFromServer(currentClickedRecipe.getGetMoreInfoUrl(), currentClickedRecipe.getRecipeId(), new ApiManager.Callback_networkResponse() {
                    @Override
                    public void onSuccess(Object object) {
                        Recipe recipe = (Recipe) object;
                        recipeArrayList.set(clickedRecipePosition, recipe);
                        recipeLiveData.setValue(recipeArrayList);
                        customItemUpdateListener.onItemUpdated();


                    }

                    @Override
                    public void onError() {
                        Log.d(Constants.LOG, "Error: ");

                    }
                });
                break;
            }
            case LoadMoreItems: {
                paginationManager.setLoading(true);
                paginationManager.setCurrentPage(paginationManager.getCurrentPage() + 1);
                break;
            }
            case LoadNextPage: {
                int count = (int) object;
                paginationManager.setLoading(false);
                paginationManager.setTotalPages(count / paginationManager.getMaxResultsInPage());
                paginationManager.removeLoadingFooter();

                if (paginationManager.getCurrentPage() != paginationManager.getTotalPages())
                    paginationManager.addLoadingFooter();
                else paginationManager.setLastPage(true);
                break;
            }
            case LoadFirstPage: {
                int count = (int) object;
                paginationManager.setTotalPages(count / paginationManager.getMaxResultsInPage());

                if (paginationManager.getCurrentPage() <= paginationManager.getTotalPages())
                    paginationManager.addLoadingFooter();
                else paginationManager.setLastPage(true);
            }
        }
    }

    public boolean isLastPage() {
        return paginationManager.isLastPage();
    }

    public boolean isLoading() {
        return paginationManager.isLoading();
    }

    public RecipeListViewModel setItemLisener(CustomItemUpdateListener customItemUpdateListener) {
        this.customItemUpdateListener = customItemUpdateListener;
        return this;
    }
}
