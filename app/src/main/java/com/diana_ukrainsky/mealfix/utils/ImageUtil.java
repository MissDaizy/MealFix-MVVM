package com.diana_ukrainsky.mealfix.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class ImageUtil {
    public static void setImageUI(Context context, String imageUrl, ImageView imageView,
                                  int width,int height) {
        Glide.with(context)
                .load(imageUrl)
                .override(width, height)
                .into(imageView);
    }
}
