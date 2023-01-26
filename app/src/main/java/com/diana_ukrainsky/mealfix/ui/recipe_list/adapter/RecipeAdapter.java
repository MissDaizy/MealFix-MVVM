package com.diana_ukrainsky.mealfix.ui.recipe_list.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.diana_ukrainsky.mealfix.data.model.recipe.Recipe;
import com.diana_ukrainsky.mealfix.databinding.ItemProgressBinding;
import com.diana_ukrainsky.mealfix.databinding.RecipeListItemBinding;
import com.diana_ukrainsky.mealfix.ui.callback.CustomItemClickListener;
import com.diana_ukrainsky.mealfix.ui.callback.RecipeDiffCallback;
import com.diana_ukrainsky.mealfix.ui.recipe_list.pagination.PaginationManager;
import com.diana_ukrainsky.mealfix.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private RecipeListItemBinding recipeListItemBinding;
    private ItemProgressBinding itemProgressBinding;

    private ArrayList<Recipe> recipeArrayList;
    private CustomItemClickListener customItemClickListener;

    private Context context;

    private static final int LOADING = 0;
    private static final int ITEM = 1;

    private static final int RECIPE_IMAGE_WIDTH=1000;
    private static final int RECIPE_IMAGE_HEIGHT=1000;

    public RecipeAdapter(Context context, CustomItemClickListener customItemClickListener) {
        this.customItemClickListener=customItemClickListener;
        this.recipeArrayList = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case ITEM:
                recipeListItemBinding = RecipeListItemBinding.inflate(layoutInflater,parent,false);
                viewHolder = new MyViewHolder (recipeListItemBinding);
                break;
            case LOADING:
                itemProgressBinding = ItemProgressBinding.inflate(layoutInflater,parent,false);
                viewHolder = new LoadingViewHolder(itemProgressBinding);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Recipe recipe = recipeArrayList.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                MyViewHolder myViewHolder = (MyViewHolder) holder;
                setListeners(myViewHolder,recipe);
                myViewHolder.recipeListItemBinding.recipeListItemTXTTitle.setText(recipe.getRecipeName());
                myViewHolder.recipeListItemBinding.recipeListItemTXTCookingTime.setText(""+recipe.getCookTime());
                setImage(recipe.getRecipeImage(),myViewHolder.recipeListItemBinding.recipeListItemIMGRecipeImage
                );


                break;

            case LOADING:
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.itemProgressBinding.loadmoreProgress.setVisibility(View.VISIBLE);
                break;
        }

    }

    public void setImage(String recipeImage, ImageView recipeListItemIMGRecipeImage) {
            ImageUtil.setImageUI(context,
                    recipeImage,
                    recipeListItemIMGRecipeImage,
                    RECIPE_IMAGE_WIDTH,
                    RECIPE_IMAGE_HEIGHT);
    }

    private void setListeners(MyViewHolder holder, Recipe recipe) {
        holder.recipeListItemBinding.recipeListItemCVRecipeItemCard.setOnClickListener(v->{
            customItemClickListener.onClick(recipe);
        });
    }

    @Override
    public int getItemViewType(int position) {
        return (position == recipeArrayList.size() - 1 && PaginationManager.getInstance().isLoadingAdded()) ? LOADING : ITEM;
    }

    @Override
    public int getItemCount() {
        return recipeArrayList.size();
    }

    public void addAll(List<Recipe> recipes) {
        this.recipeArrayList.addAll(recipes);
        notifyDataSetChanged();
    }

    public void updateRecipeListItems(List<Recipe> recipes) {
        final RecipeDiffCallback diffCallback = new RecipeDiffCallback(this.recipeArrayList, recipes);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.recipeArrayList.clear();
        this.recipeArrayList.addAll(recipes);
        this.notifyDataSetChanged();
        diffResult.dispatchUpdatesTo(this);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private RecipeListItemBinding recipeListItemBinding;

        public MyViewHolder(RecipeListItemBinding recipeListItemBinding) {
            super(recipeListItemBinding.getRoot());
            this.recipeListItemBinding = recipeListItemBinding;

        }
    }
    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ItemProgressBinding itemProgressBinding;

        public LoadingViewHolder(ItemProgressBinding itemProgressBinding) {
            super(itemProgressBinding.getRoot());
            this.itemProgressBinding = itemProgressBinding;

        }
    }
}
