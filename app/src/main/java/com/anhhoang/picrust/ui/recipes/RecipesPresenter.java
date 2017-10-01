package com.anhhoang.picrust.ui.recipes;

import com.anhhoang.picrust.data.models.RecipeModel;
import com.anhhoang.picrust.data.source.BaseDataSource;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by anh.hoang on 9/23/17.
 */

public class RecipesPresenter implements RecipesContracts.Presenter {
    private RecipesContracts.View view;
    private BaseDataSource<RecipeModel> repository;

    public RecipesPresenter(RecipesContracts.View view, BaseDataSource<RecipeModel> repository) {
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
        view.showErrorView(false, null);
        repository.get(new BaseDataSource.ResultsCallback<RecipeModel>() {
            @Override
            public void onLoaded(List<RecipeModel> result) {
                view.showLoadingIndicator(false);
                view.showErrorView(false, null);
                view.showRecipes(result);
            }

            @Override
            public void onDataNotAvailable(Object additionalInfo) {
                RecipeErrorEnum recipeErrorEnum = RecipeErrorEnum.EMPTY;
                if (additionalInfo == null) {
                    recipeErrorEnum = RecipeErrorEnum.EMPTY;
                } else if (additionalInfo instanceof Integer) {
                    recipeErrorEnum = RecipeErrorEnum.OTHER; // Server error
                } else if (additionalInfo instanceof Throwable) {
                    recipeErrorEnum = RecipeErrorEnum.NETWORK; // needs more testing to verify
                }

                view.showLoadingIndicator(false);
                view.showErrorView(true, recipeErrorEnum);
            }
        });
    }

    @Override
    public void openRecipeDetail(RecipeModel recipe) {
        view.showRecipeDetail(recipe.recipe.getId());
    }
}
