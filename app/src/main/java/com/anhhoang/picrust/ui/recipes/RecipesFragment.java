package com.anhhoang.picrust.ui.recipes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.anhhoang.picrust.R;
import com.anhhoang.picrust.data.models.RecipeModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipesFragment extends Fragment implements RecipesContracts.View, RecipesAdapter.OnClickListener {
    @BindView(R.id.recipes_recycler_view)
    RecyclerView rvRecipes;
    @BindView(R.id.error_view)
    View errorView;
    @BindView(R.id.error_text_view)
    TextView tvError;
    @BindView(R.id.error_description_text_view)
    TextView tvErrorDescription;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private RecipesContracts.Presenter presenter;
    private RecipesAdapter recipesAdapter;

    public RecipesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        ButterKnife.bind(this, view);

        recipesAdapter = new RecipesAdapter(null, R.layout.recipe_item_list_view, this);
        rvRecipes.setAdapter(recipesAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setPresenter(RecipesContracts.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoadingIndicator(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showRecipes(List<RecipeModel> recipes) {
        recipesAdapter.setRecipes(recipes);
    }

    @Override
    public void showErrorView(boolean hasError, RecipeErrorEnum errorType) {
        if (hasError) {
            String errorText;
            String errorDescriptionText;
            // TODO: Show Error text

            errorView.setVisibility(View.VISIBLE);
        } else {
            errorView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showRecipeDetail(int recipeId) {
        // TODO: Switch to detail Activity
    }

    @Override
    public void onClick(RecipeModel recipeModel) {
        presenter.openRecipeDetail(recipeModel);
    }
}
