package com.diana_ukrainsky.mealfix.presentation.recipe_list;

public class PaginationManager {

    private static PaginationManager INSTANCE = null;
    // ***Pagination Implementation***
    private boolean isLoadingAdded = false;

    private final int MAX_RESULTS_IN_PAGE = 20;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int totalPages;
    private int currentPage = PAGE_START;

    private PaginationManager() {
    }

    // Static method
    // Static method to create instance of Singleton class
    public static PaginationManager getInstance() {
        if (INSTANCE == null)
            INSTANCE = new PaginationManager();

        return INSTANCE;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean isLastPage() {
        return isLastPage;
    }



    public void addLoadingFooter() {
        isLoadingAdded = true;
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
    }

    public int getMaxResultsInPage() {
        return MAX_RESULTS_IN_PAGE;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public boolean isLoadingAdded() {
        return isLoadingAdded;
    }

    public void setLoadingAdded(boolean loadingAdded) {
        isLoadingAdded = loadingAdded;
    }
}
