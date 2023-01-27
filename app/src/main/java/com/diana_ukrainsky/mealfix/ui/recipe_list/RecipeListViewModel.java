package com.diana_ukrainsky.mealfix.ui.recipe_list;

import static com.diana_ukrainsky.mealfix.ui.recipe_list.FilterType.ALL;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diana_ukrainsky.mealfix.common.Constants;
import com.diana_ukrainsky.mealfix.data.model.recipe.Recipe;
import com.diana_ukrainsky.mealfix.data.model.recipe.RecipeList;
import com.diana_ukrainsky.mealfix.repository.Repository;
import com.diana_ukrainsky.mealfix.ui.recipe_list.pagination.PaginationManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

@HiltViewModel
public class RecipeListViewModel extends ViewModel {
    // Variable the Recipe List
    private MutableLiveData<List<Recipe>> recipeLiveData;
    private MutableLiveData<List<Recipe>> filteredLiveData;
    // Variable of the selectedRecipe
    private MutableLiveData<Recipe> selectedRecipe;
    private MutableLiveData<Boolean> isSelectedRecipeUpdated;

    // Variable for hiding and showing the loading spinner
    private MutableLiveData<Boolean> loading;

    private FilterType selectedFilter;

    private PublishSubject<RecipeList> recipesSubject;

    private CompositeDisposable disposables;

    ArrayList<Recipe> recipeArrayList;
    private Repository repository;
    private PaginationManager paginationManager;

    @Inject
    public RecipeListViewModel(Repository repository) {
        this.repository = repository;


        init();
        subscribeSubject();
    }

    private void subscribeSubject() {
        Disposable disposable =
                repository.getRecipes(getCurrentPage(), getMaxResultsInPage())
                        .subscribeOn(Schedulers.io())
                        .subscribe(recipesSubject::onNext, throwable -> {
                            // handle error here
                        });
        disposables.add(disposable);
    }

    public MutableLiveData<List<Recipe>> getRecipeMutableLiveData() {
        return recipeLiveData;
    }

    public void init() {
        recipeLiveData = new MutableLiveData<>();
        selectedRecipe = new MutableLiveData<>();
        isSelectedRecipeUpdated = new MutableLiveData<>();
        loading = new MutableLiveData<>();
        filteredLiveData = new MutableLiveData<>();
        recipeArrayList = new ArrayList<>();

        recipesSubject = PublishSubject.create();
        disposables = new CompositeDisposable();

        paginationManager = PaginationManager.getInstance();
        recipeLiveData.setValue(recipeArrayList);

        // Default of filter is "All"
        selectedFilter = ALL;
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

    public MutableLiveData<Boolean> getIsSelectedRecipeUpdated() {
        return isSelectedRecipeUpdated;
    }

    public MutableLiveData<List<Recipe>> getRecipeListData() {
        if (recipeLiveData == null) {
            recipeLiveData = new MutableLiveData<>();
        }

        return recipeLiveData;
    }

    public MutableLiveData<List<Recipe>> getFilteredLiveData() {
        return filteredLiveData;
    }

    public void getRecipes() {
        // Check if there are more recipes to fetch
        if (!isLastPage()) {
            // Shows loading spinner
            loading.setValue(true);
            // Get Recipes by page
            recipesSubject
                    .debounce(400, TimeUnit.MILLISECONDS)
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
                            disposables.add(d);
                        }

                        @Override
                        public void onNext(@NonNull RecipeList recipeList) {
                            // Hides loading spinner and update the UI with the recipe data
                            loading.setValue(false);
                            // I have to clear that list:
                            recipeArrayList.clear();
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
                            disposables.add(d);
                        }

                        @Override
                        public void onNext(@NonNull Recipe recipe) {
                            // Hides loading spinner and update the UI with the additional recipe attributes
                            loading.setValue(false);
                            recipe.setAdditionalAttributes(recipe);
                            selectedRecipe.setValue(recipe);
                            isSelectedRecipeUpdated.setValue(true);
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
            case FilterList: {
                selectedFilter = (FilterType) object;
                filterList();
                break;
            }
            case ListItemClicked: {
                Recipe currentRecipe = (Recipe) object;
                selectedRecipe.setValue(currentRecipe);
                selectRecipe(currentRecipe);

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

    private void filterList() {
        List<Recipe> filteredRecipes;
        if (recipeLiveData.getValue() == null)
            return;
        else if (filteredLiveData.getValue() == null)
            filteredRecipes = recipeLiveData.getValue();
        else
            filteredRecipes = filteredLiveData.getValue();

        filterCases(filteredRecipes);
    }

    private void filterCases(List<Recipe> filteredRecipes) {
        switch (selectedFilter) {
            case ALL:
                filteredLiveData.setValue(recipeLiveData.getValue());
                break;
            case ASC_COOK_TIME:
                Collections.sort(filteredRecipes, new Recipe.SortByCookTime().reversed());
                filteredLiveData.setValue(filteredRecipes);
                break;
            case DESC_COOK_TIME:
                Collections.sort(filteredRecipes, new Recipe.SortByCookTime());
                filteredLiveData.setValue(filteredRecipes);
                break;
            case TITLE:
                Collections.sort(filteredRecipes, new Recipe.SortByTitle());
                filteredLiveData.setValue(filteredRecipes);
                break;
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
        paginationManager.setCurrentPage(getCurrentPage() + 1);
    }

    // TODO: change and add equals function of objects
    public void searchRecipes(String searchQuery) {
        List<Recipe> filteredRecipes = new ArrayList<>();
        for (Recipe recipe : Objects.requireNonNull(recipeLiveData.getValue())) {
            if (recipe.getRecipeName().toLowerCase().contains(searchQuery)) {
                filteredRecipes.add(recipe);
            }
        }
        filteredLiveData.setValue(filteredRecipes);
    }

    public void disposeComposite() {
        // Using dispose will clear all and set isDisposed = true, so it will not accept any new disposable
        disposables.dispose();
    }
}
