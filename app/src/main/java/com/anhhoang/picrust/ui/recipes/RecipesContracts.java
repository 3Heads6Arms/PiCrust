package com.anhhoang.picrust.ui.recipes;

import com.anhhoang.picrust.BasePresenter;
import com.anhhoang.picrust.BaseView;
import com.anhhoang.picrust.data.Recipe;

import java.util.List;

/**
 * Created by anh.hoang on 9/23/17.
 */

public interface RecipesContracts {

    interface View extends BaseView<Presenter> {
        void showLoadingIndicator(boolean isLoading);

        void showRecipes(List<Recipe> recipes);

        void showErrorView(boolean hasError, RecipeErrorEnum errorType);

        void showRecipeDetail(long recipeId);
    }

    interface Presenter extends BasePresenter {
        void loadRecipes(boolean forceUpdate);

        void openRecipeDetail(Recipe recipe);
    }
}
