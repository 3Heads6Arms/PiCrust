package com.anhhoang.picrust.ui.recipes;

import com.anhhoang.picrust.data.models.RecipeModel;
import com.anhhoang.picrust.data.source.BaseDataSource;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by anh.hoang on 9/23/17.
 */

class RecipesPresenter implements RecipesContracts.Presenter {
    private RecipesContracts.View view;
    private BaseDataSource<RecipeModel> repository;

    RecipesPresenter(RecipesContracts.View view, BaseDataSource<RecipeModel> repository) {
        this.view = checkNotNull(view);
        this.repository = checkNotNull(repository);

        view.setPresenter(this);
    }

    @Override
    public void start() {
        loadRecipes(true);
    }

    @Override
    public void loadRecipes(boolean forceUpdate) {
        if (forceUpdate) {
            repository.refresh();
        }

        view.showLoadingIndicator(true);
        repository.get(new BaseDataSource.ResultsCallback<RecipeModel>() {
            @Override
            public void onLoaded(List<RecipeModel> result) {
                view.showRecipes(result);
                view.showLoadingIndicator(false);
            }

            @Override
            public void onDataNotAvailable() {
                view.showErrorView(true, RecipeErrorEnum.EMPTY);
                view.showLoadingIndicator(false);
            }
        });
    }

    @Override
    public void openRecipeDetail(RecipeModel recipe) {
        // TODO:
    }
}