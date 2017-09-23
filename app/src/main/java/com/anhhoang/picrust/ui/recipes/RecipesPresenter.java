package com.anhhoang.picrust.ui.recipes;

import com.anhhoang.picrust.data.Recipe;
import com.anhhoang.picrust.data.source.RecipesRepository;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by anh.hoang on 9/23/17.
 */

public class RecipesPresenter implements RecipesContracts.Presenter {
    private RecipesContracts.View view;
    private RecipesRepository repository;

    public RecipesPresenter(RecipesContracts.View view, RecipesRepository repository) {
        this.view = checkNotNull(view);
        this.repository = checkNotNull(repository);

        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadRecipes(boolean forceUpdate) {

    }

    @Override
    public void openRecipeDetail(Recipe recipe) {

    }
}
