package com.anhhoang.picrust.ui.ingredients;

import com.anhhoang.picrust.data.Ingredient;

import java.util.List;

/**
 * Created by anh.hoang on 9/27/17.
 */

public class IngredientsPresenter implements IngredientsContracts.Presenter {

    private final IngredientsContracts.View view;
    private final List<Ingredient> ingredients;

    public IngredientsPresenter(IngredientsContracts.View view, List<Ingredient> ingredients) {
        this.view = view;
        this.ingredients = ingredients;

        view.setPresenter(this);
    }

    @Override
    public void start() {
        loadIngredients();
    }

    @Override
    public void loadIngredients() {
        if (ingredients == null || ingredients.size() <= 0) {
            view.showError(true);
        } else {
            view.showError(false);
            view.showIngredients(ingredients);
        }
    }
}
