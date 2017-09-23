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
import com.anhhoang.picrust.data.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipesFragment extends Fragment implements RecipesContracts.View {
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

    public RecipesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        ButterKnife.bind(view);

        return view;
    }

    @Override
    public void setPresenter(RecipesContracts.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showRecipes(List<Recipe> recipes) {

    }

    @Override
    public void setErrorView(boolean hasError, RecipeErrorEnum errorType) {

    }

    @Override
    public void showRecipeDetail(int recipeId) {

    }
}
