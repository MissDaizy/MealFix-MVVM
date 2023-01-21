package com.diana_ukrainsky.mealfix.ui.recipe_list;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diana_ukrainsky.mealfix.common.Constants;
import com.diana_ukrainsky.mealfix.data.model.recipe.Recipe;
import com.diana_ukrainsky.mealfix.data.model.recipe.RecipeList;
import com.diana_ukrainsky.mealfix.data.remote.ApiManager;
import com.diana_ukrainsky.mealfix.data.callback.callbacks.Callback_retrofitResponse;
import com.diana_ukrainsky.mealfix.data.remote.ApiService;
import com.diana_ukrainsky.mealfix.ui.recipe_list.pagination.PaginationManager;
import com.diana_ukrainsky.repository.Repository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class RecipeListViewModel extends ViewModel {
    // Variable the Recipe List
    private MutableLiveData<List<Recipe>> recipeLiveData;
    // Variable of the selectedRecipe
    private MutableLiveData<Recipe> selectedRecipe;
    // Variable for hiding and showing the loading spinner
    private MutableLiveData<Boolean> loading;

    ArrayList<Recipe> recipeArrayList;
    Recipe currentClickedRecipe;
    int clickedRecipePosition;

    private ApiService apiService;
    private Repository repository;

    ApiManager apiManager;
    private PaginationManager paginationManager;

    @Inject
    public RecipeListViewModel(Repository repository) {
        this.repository = repository;

        recipeLiveData = new MutableLiveData<>();
        selectedRecipe = new MutableLiveData<>();
        loading = new MutableLiveData<>();

        init();
    }

    public MutableLiveData<List<Recipe>> getRecipeMutableLiveData() {
        return recipeLiveData;
    }

    public void init() {
        apiService = ApiService.getInstance();
        paginationManager = PaginationManager.getInstance();
        recipeArrayList = new ArrayList<>();
        recipeLiveData.setValue(recipeArrayList);
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<List<Recipe>> getRecipesList() {
        return recipeLiveData;
    }

    public LiveData<Recipe> getSelectedRecipe() {
        return selectedRecipe;
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

    public MutableLiveData<List<Recipe>> getRecipeListData() {
        if (recipeLiveData == null) {
            recipeLiveData = new MutableLiveData<>();
        }

        return recipeLiveData;
    }

    public void getRecipes() {
        // Check if there are more recipes to fetch
        if(!isLastPage()) {
            // Shows loading spinner
            loading.setValue(true);
            // Get Recipes by page
            repository.getRecipes(getCurrentPage(),getMaxResultsInPage())
                    .subscribeOn(Schedulers.io())
                    .map(recipeList -> {
                        for (Recipe recipe : recipeList.getRecipeList()) {
                            String recipeMoreInfoUrl = recipe.getGetMoreInfoUrl();
                            String recipeUrl = recipeMoreInfoUrl.substring(recipeMoreInfoUrl.lastIndexOf("/") + 1);
                            recipe.setGetMoreInfoUrl(recipeUrl);
                        }
                        return recipeList;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<RecipeList>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            // Shows loading spinner
                            loading.setValue(true);
                        }

                        @Override
                        public void onNext(@NonNull RecipeList recipeList) {
                            // Hides loading spinner and update the UI with the recipe data
                            loading.setValue(false);
                            recipeArrayList.addAll(recipeList.getRecipeList());
                            recipeLiveData.setValue(recipeArrayList);
                            incrementCurrentPage();
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            // Hides loading spinner and show an error message
                            loading.setValue(false);
                            Log.e(Constants.LOG, "getRecipes: " + e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            // Nothing to do here
                        }
                    });
        }
    }


    public void getRecipeDetails(String recipeUrl, int id, ApiManager.Callback_networkResponse callback_networkResponse) {
        recipeUrl = recipeUrl.substring(recipeUrl.lastIndexOf("/") + 1);
        Observable<Recipe> call = apiService.getJsonApiRecipe().getRecipeDetails(
                recipeUrl
        );

        // TODO: Change to Rxjava3:

//        call.enqueue(new Callback<Recipe>() {
//            @Override
//            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
//                if (response.isSuccessful()) {
//                    assert response.body() != null;
//                    callback_networkResponse.onSuccess(response.body());
//                } else {
//                    callback_secondNetworkResponse.onError();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Recipe> call, Throwable t) {
//                callback_firstNetworkResponse.onError();
//            }
//        });
    }

    public void selectRecipe(Recipe recipe) {
        if (recipeLiveData.getValue() != null) {
            loading.setValue(true);
            repository.getRecipeDetails(recipe)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Recipe>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            // Shows loading spinner
                            loading.setValue(true);
                        }

                        @Override
                        public void onNext(@NonNull Recipe recipe) {
                            // Hides loading spinner and update the UI with the additional recipe attributes
                            loading.setValue(false);
                            recipe.setAdditionalAttributes(recipe);
                            selectedRecipe.setValue(recipe);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            // Hides loading spinner
                            loading.setValue(false);
                        }

                        @Override
                        public void onComplete() {
                            // Nothing to do here

                        }
                    });
        }
    }

    public void onEventRecipeList(RecipeListEvent event, Object object) {
        switch (event) {
            case ListItemClicked: {

                selectRecipe((Recipe) object);

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

    public int getCurrentPage() {
        return paginationManager.getCurrentPage();
    }
    public int getMaxResultsInPage() {
        return paginationManager.getMaxResultsInPage();
    }

    private void incrementCurrentPage() {
        paginationManager.setCurrentPage(getCurrentPage()+1);
    }

}
