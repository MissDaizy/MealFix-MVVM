package com.diana_ukrainsky.mealfix.presentation.recipe_list;

public class CustomEventHandler {

    // Step 2- This variable represents the listener passed in by the owning object
    // The listener must implement the events interface and passes messages up to the parent.
    private CustomObjectListener customObjectListener;

    // Constructor where listener events are ignored
    public CustomEventHandler() {
        this.customObjectListener = null; // set null listener
    }

    // Assign the listener implementing events interface that will receive the events (passed in by the owner)
    public void setCustomObjectListener(CustomObjectListener customObjectListener) {
        this.customObjectListener = customObjectListener;
    }

    public CustomObjectListener getCustomEventListener() {
        return customObjectListener;
    }

    public CustomObjectListener getCustomObjectListener() {
        return customObjectListener;
    }

}
