package com.diana_ukrainsky.mealfix.ui.recipe_list;

public enum FilterType {
    ALL,ASC_COOK_TIME,DESC_COOK_TIME,TITLE;

    public String getFilterType() {

        // this will refer to the object SMALL
        switch (this) {
            case ALL:
                return "All";

            case ASC_COOK_TIME:
                return "Ascending Cook Time";

            case DESC_COOK_TIME:
                return "Descending Cook Time";

            case TITLE:
                return "Title";

            default:
                return null;
        }
    }
}
