package com.anhhoang.picrust.ui.recipes;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anhhoang.picrust.R;
import com.anhhoang.picrust.data.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by anh.hoang on 9/24/17.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {
    private List<Recipe> recipes;
    private int itemViewRes;
    private OnClickListener onClickListener;

    public RecipesAdapter(List<Recipe> recipes, int itemViewRes, OnClickListener onClickListener) {
        this.recipes = recipes;
        this.itemViewRes = itemViewRes;
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        boolean attachToRoot = false;

        View view = LayoutInflater.from(parent.getContext())
                .inflate(itemViewRes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe recipeModel = recipes.get(position);
        holder.itemView.setTag(recipeModel);
        holder.tvRecipeName.setText(recipeModel.getName());

        if (!TextUtils.isEmpty(recipeModel.getImage())) {
            Picasso.with(holder.itemView.getContext())
                    .load(recipeModel.getImage())
                    .centerInside()
                    .error(R.drawable.ic_image_black_24dp)
                    .into(holder.ivRecipeThumbnail);
        }
    }

    @Override
    public int getItemCount() {
        return recipes == null ? 0 : recipes.size();
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public interface OnClickListener {
        void onClick(Recipe recipeModel);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recipe_name_text_view)
        TextView tvRecipeName;
        @BindView(R.id.recipe_thumbnail_image_view)
        ImageView ivRecipeThumbnail;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClick((Recipe) itemView.getTag());
                }
            });
        }
    }
}
