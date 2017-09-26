package com.anhhoang.picrust.ui.recipedetails;

import com.anhhoang.picrust.BasePresenter;
import com.anhhoang.picrust.BaseView;
import com.anhhoang.picrust.data.Ingredient;
import com.anhhoang.picrust.data.Step;
import com.anhhoang.picrust.data.models.RecipeItem;
import com.anhhoang.picrust.data.models.RecipeModel;

import java.util.List;

/**
 * Created by anh.hoang on 9/24/17.
 */

public class RecipeDetailContracts {
    interface View extends BaseView<Presenter> {
        void showIngredients(List<Ingredient> ingredients);

        void showStep(int stepId, List<Step> steps);

        void showDetail(RecipeModel recipeModel);

        void showLoadingIndicator(boolean isLoading);

        void showError(boolean hasError);
    }

    interface Presenter extends BasePresenter {
        void loadRecipe(int recipeId);

        void openStepDetail(int stepId, List<RecipeItem> recipeItems, Class tClass);
    }
}
