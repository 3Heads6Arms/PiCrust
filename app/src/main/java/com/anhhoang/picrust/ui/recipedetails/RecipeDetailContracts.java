package com.anhhoang.picrust.ui.recipedetails;

import com.anhhoang.picrust.BasePresenter;
import com.anhhoang.picrust.BaseView;
import com.anhhoang.picrust.data.Recipe;
import com.anhhoang.picrust.data.Step;
import com.anhhoang.picrust.data.models.RecipeModel;

/**
 * Created by anh.hoang on 9/24/17.
 */

public class RecipeDetailContracts {
    interface View extends BaseView<Presenter> {
        void showIngredients(int recipeId);

        void showStep(int stepId, int recipeId);

        void showDetail(RecipeModel recipeModel);

        void showLoadingIndicator(boolean isLoading);
    }

    interface Presenter extends BasePresenter {
        void loadRecipe();

        void openIngredients(Recipe recipe);

        void openStep(Step step);
    }
}
