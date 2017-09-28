package com.anhhoang.picrust.ui.recipes;

import com.anhhoang.picrust.BasePresenter;
import com.anhhoang.picrust.BaseView;
import com.anhhoang.picrust.data.models.RecipeModel;

import java.util.List;

/**
 * Created by anh.hoang on 9/23/17.
 */

interface RecipesContracts {

    interface View extends BaseView<Presenter> {
        void showLoadingIndicator(boolean isLoading);

        void showRecipes(List<RecipeModel> recipes);

        void showErrorView(boolean hasError, RecipeErrorEnum errorType);

        void showRecipeDetail(int recipeId);
    }

    interface Presenter extends BasePresenter {
        void loadRecipes(boolean forceUpdate);

        void openRecipeDetail(RecipeModel recipe);
    }
}
