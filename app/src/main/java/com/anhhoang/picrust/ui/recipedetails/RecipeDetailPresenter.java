package com.anhhoang.picrust.ui.recipedetails;

import com.anhhoang.picrust.data.Ingredient;
import com.anhhoang.picrust.data.Recipe;
import com.anhhoang.picrust.data.Step;
import com.anhhoang.picrust.data.models.RecipeItem;
import com.anhhoang.picrust.data.source.BaseDataSource;
import com.anhhoang.picrust.ui.recipedetails.RecipeDetailContracts.Presenter;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by anh.hoang on 9/24/17.
 */

public class RecipeDetailPresenter implements Presenter {
    private final RecipeDetailContracts.View view;
    private final BaseDataSource<Recipe> repository;
    private final long recipeId;

    public RecipeDetailPresenter(RecipeDetailContracts.View view, BaseDataSource<Recipe> repository, long recipeId) {
        this.view = view;
        this.repository = repository;
        this.recipeId = recipeId;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        loadRecipe(recipeId);
    }

    @Override
    public void loadRecipe(long recipeId) {
        view.showLoadingIndicator(true);
        repository.get(recipeId, new BaseDataSource.ResultCallback<Recipe>() {
            @Override
            public void onLoaded(Recipe result) {
                checkNotNull(result, "Detail screen, data cannot be null");
                view.showDetail(result);
                view.showLoadingIndicator(false);
                if (result.getSteps() == null || result.getSteps().size() <= 0) {
                    view.showError(true);
                }
            }

            @Override
            public void onDataNotAvailable(Object additionalInfo) {
                view.showLoadingIndicator(false);
                view.showError(true);
            }
        });
    }


    @Override
    public void openStepDetail(long stepId, List<RecipeItem> recipeItems, Class tClass) {
        if (Step.class == tClass) {
            view.showStep(stepId, transformList(recipeItems, Step.class));
        } else if (Ingredient.class == tClass) {
            view.showIngredients(transformList(recipeItems, Ingredient.class));
        }
    }

    private <T> List<T> transformList(List<RecipeItem> input, Class<T> tClass) {
        List<T> result = new ArrayList<>();
        for (RecipeItem item : input) {
            result.add((T) item);
        }

        return result;
    }
}
