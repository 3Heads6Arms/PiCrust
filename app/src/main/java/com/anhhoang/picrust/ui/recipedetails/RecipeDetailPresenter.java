package com.anhhoang.picrust.ui.recipedetails;

import com.anhhoang.picrust.data.Recipe;
import com.anhhoang.picrust.data.Step;
import com.anhhoang.picrust.data.models.RecipeModel;
import com.anhhoang.picrust.data.source.BaseDataSource;
import com.anhhoang.picrust.ui.recipedetails.RecipeDetailContracts.Presenter;

/**
 * Created by anh.hoang on 9/24/17.
 */

public class RecipeDetailPresenter implements Presenter {
    private final RecipeDetailContracts.View view;
    private final BaseDataSource<RecipeModel> repository;

    public RecipeDetailPresenter(RecipeDetailContracts.View view, BaseDataSource<RecipeModel> repository) {
        this.view = view;
        this.repository = repository;
        view.setPresenter(this);
    }

    @Override
    public void start() {
    }

    @Override
    public void loadRecipe(int recipeId) {
        repository.get(recipeId, new BaseDataSource.ResultCallback<RecipeModel>() {
            @Override
            public void onLoaded(RecipeModel result) {
                // TODO: show data
            }

            @Override
            public void onDataNotAvailable(Object additionalInfo) {
                // TODO: Show problem
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
