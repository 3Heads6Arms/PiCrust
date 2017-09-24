package com.anhhoang.picrust.ui.recipedetails;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anhhoang.picrust.R;
import com.anhhoang.picrust.data.models.RecipeModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeDetailFragment extends Fragment implements RecipeDetailContracts.View {

    private RecipeDetailContracts.Presenter presenter;

    public RecipeDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false);
    }

    @Override
    public void setPresenter(RecipeDetailContracts.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showIngredients(int recipeId) {
        // TODO:
    }

    @Override
    public void showStep(int stepId, int recipeId) {
        // TODO:
    }

    @Override
    public void showDetail(RecipeModel recipeModel) {
        // TODO: load to menu
    }

    @Override
    public void showLoadingIndicator(boolean isLoading) {
        // TODO:
    }
}
