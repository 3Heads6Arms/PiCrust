package com.anhhoang.picrust.ui.recipes;

import android.support.annotation.Nullable;

import com.anhhoang.picrust.SimpleIdlingResource;
import com.anhhoang.picrust.data.Recipe;
import com.anhhoang.picrust.data.source.BaseDataSource;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by anh.hoang on 9/23/17.
 */

public class RecipesPresenter implements RecipesContracts.Presenter {
    private final SimpleIdlingResource idlingResource;
    private RecipesContracts.View view;
    private BaseDataSource<Recipe> repository;

    public RecipesPresenter(RecipesContracts.View view, BaseDataSource<Recipe> repository, @Nullable SimpleIdlingResource idlingResource) {
        this.view = checkNotNull(view);
        this.repository = checkNotNull(repository);
        this.idlingResource = idlingResource;
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

        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }
        view.showLoadingIndicator(true);
        view.showErrorView(false, null);
        repository.get(new BaseDataSource.ResultsCallback<Recipe>() {
            @Override
            public void onLoaded(List<Recipe> result) {
                view.showLoadingIndicator(false);
                view.showErrorView(false, null);
                view.showRecipes(result);
                idlingResource.setIdleState(true);
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
                idlingResource.setIdleState(true);
            }
        });
    }

    @Override
    public void openRecipeDetail(Recipe recipe) {
        view.showRecipeDetail(recipe.getId());
    }
}
