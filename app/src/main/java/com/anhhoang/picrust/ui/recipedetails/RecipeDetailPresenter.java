package com.anhhoang.picrust.ui.recipedetails;

import com.anhhoang.picrust.data.Recipe;
import com.anhhoang.picrust.data.Step;
import com.anhhoang.picrust.data.models.RecipeModel;
import com.anhhoang.picrust.data.source.BaseDataSource;
import com.anhhoang.picrust.ui.recipedetails.RecipeDetailContracts.Presenter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by anh.hoang on 9/24/17.
 */

public class RecipeDetailPresenter implements Presenter {
    private final RecipeDetailContracts.View view;
    private final BaseDataSource<RecipeModel> repository;
    private final int recipeId;

    public RecipeDetailPresenter(RecipeDetailContracts.View view, BaseDataSource<RecipeModel> repository, int recipeId) {
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
    public void loadRecipe(int recipeId) {
        repository.get(recipeId, new BaseDataSource.ResultCallback<RecipeModel>() {
            @Override
            public void onLoaded(RecipeModel result) {
                checkNotNull(result, "Detail screen, data cannot be null");
                view.showDetail(result);
            }

            @Override
            public void onDataNotAvailable(Object additionalInfo) {
                throw new RuntimeException("Detail screen must have recipe data.");
            }
        });
    }

    @Override
    public void openIngredients(Recipe recipe) {
        // TODO: Show ingredient activity
    }

    @Override
    public void openStep(Step step) {
        // TODO: Show step activity
    }
}
