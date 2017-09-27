package com.anhhoang.picrust.ui.ingredients;

import com.anhhoang.picrust.BasePresenter;
import com.anhhoang.picrust.BaseView;
import com.anhhoang.picrust.data.Ingredient;

import java.util.List;

/**
 * Created by anh.hoang on 9/27/17.
 */

public class IngredientsContracts {
    interface View extends BaseView<Presenter> {
        void showIngredients(List<Ingredient> ingredients);

        void showLoadingIndicator(boolean isLoading);

        void showError(boolean hasError);
    }

    interface Presenter extends BasePresenter {
        void loadIngredients();
    }
}
