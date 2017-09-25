package com.anhhoang.picrust.ui.recipedetails;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anhhoang.picrust.R;
import com.anhhoang.picrust.data.Ingredient;
import com.anhhoang.picrust.data.Step;
import com.anhhoang.picrust.data.models.RecipeItem;
import com.anhhoang.picrust.data.models.RecipeModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by anh.hoang on 9/25/17.
 */

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.ViewHolder> {

    private final OnItemClickListener onItemClickListener;
    private final RecipeModel recipeModel;
    private List<RecipeItem> steps;

    public RecipeDetailAdapter(RecipeModel recipeModel, OnItemClickListener onItemClickListener) {
        this.recipeModel = recipeModel;
        setupSteps(recipeModel);
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        boolean attachToRoot = false;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_item_view, parent, attachToRoot);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (steps.get(position) instanceof Step) {
            Step step = (Step) steps.get(position);
            holder.tvItemName.setText(step.getShortDescription());
        } else {
            // position should be 0, where item should display ingredients
            holder.tvItemName.setText(R.string.ingredients);
        }
    }

    @Override
    public int getItemCount() {
        return this.steps == null ? 0 : this.steps.size();
    }

    public void setRecipeModel(RecipeModel recipeModel) {
        setupSteps(recipeModel);
        notifyDataSetChanged();
    }

    private void setupSteps(RecipeModel recipeModel) {
        if (recipeModel != null) {
            this.steps = new ArrayList<RecipeItem>(recipeModel.steps);
            // Add new placeholder Ingredient to the present it as item
            this.steps.add(0, new Ingredient(0, null, null, 0));
        } else {
            this.steps = null;
        }
    }

    interface OnItemClickListener {
        void onClick(int stepId, List<RecipeItem> items, Class tClass);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_name_text_view)
        public TextView tvItemName;

        public ViewHolder(final View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemView.getTag() instanceof Step) {
                        Step step = (Step) itemView.getTag();
                        onItemClickListener.onClick(step.getId(), new ArrayList<RecipeItem>(recipeModel.steps), Step.class);
                    } else {
                        onItemClickListener.onClick(0, new ArrayList<RecipeItem>(recipeModel.ingredients), Ingredient.class);
                    }
                }
            });
        }
    }
}
