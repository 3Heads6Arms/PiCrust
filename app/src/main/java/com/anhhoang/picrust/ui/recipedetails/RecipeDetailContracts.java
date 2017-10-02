package com.anhhoang.picrust.ui.recipedetails;

import com.anhhoang.picrust.BasePresenter;
import com.anhhoang.picrust.BaseView;
import com.anhhoang.picrust.data.Ingredient;
import com.anhhoang.picrust.data.Recipe;
import com.anhhoang.picrust.data.Step;
import com.anhhoang.picrust.data.models.RecipeItem;

import java.util.List;

/**
 * Created by anh.hoang on 9/24/17.
 */

public interface RecipeDetailContracts {
    interface View extends BaseView<Presenter> {
        void showIngredients(List<Ingredient> ingredients);

        void showStep(long stepId, List<Step> steps);

        void showDetail(Recipe recipeModel);

        void showLoadingIndicator(boolean isLoading);

        void showError(boolean hasError);
    }

    interface Presenter extends BasePresenter {
        void loadRecipe(long recipeId);

        void openStepDetail(long stepId, List<RecipeItem> recipeItems, Class tClass);
    }
}
