package com.diana_ukrainsky.mealfix.data.remote.callbacks;

public interface Callback_retrofitResponse<T> {
    void onResult(T object);
}
