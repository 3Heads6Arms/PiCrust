package com.anhhoang.picrust.ui.recipes;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.anhhoang.picrust.R;
import com.anhhoang.picrust.data.models.RecipeModel;
import com.anhhoang.picrust.ui.recipedetails.RecipeDetailActivity;

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
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        ButterKnife.bind(this, view);
        Configuration configuration = getContext().getResources().getConfiguration();

        if (recipesAdapter == null) { // fragment instance is retained, may not required to create new adapter
            recipesAdapter = new RecipesAdapter(null, R.layout.recipe_item_view, this);
        }

        rvRecipes.setAdapter(recipesAdapter);
        rvRecipes.setHasFixedSize(true);
        // Set displaying option
        if (configuration.smallestScreenWidthDp < 600) {
            rvRecipes.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        } else {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) rvRecipes.getLayoutManager();

            if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                gridLayoutManager.setSpanCount(3);
            } else {
                gridLayoutManager.setSpanCount(4);
            }
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setPresenter(RecipesContracts.Presenter presenter) {
        // When fragment instance is retained, the presenter might not be null.
        // The fragment can continue using the old one, or operate with the new one.
        // The repository is singleton and have implemented "caching" and won't do another db call.
        this.presenter = presenter;
    }

    @Override
    public void showLoadingIndicator(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            rvRecipes.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            rvRecipes.setVisibility(View.VISIBLE);
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

            switch (errorType) {
                case NETWORK:
                    errorText = getString(R.string.error_no_internet);
                    errorDescriptionText = getString(R.string.error_no_internet_description);
                    break;
                case OTHER:
                    errorText = getString(R.string.error_other);
                    errorDescriptionText = getString(R.string.error_other_description);
                    break;
                case EMPTY:
                default:
                    errorText = getString(R.string.empty_recipes_list);
                    errorDescriptionText = getString(R.string.empty_recipes_list_description);
                    break;
            }

            tvError.setText(errorText);
            tvErrorDescription.setText(errorDescriptionText);

            errorView.setVisibility(View.VISIBLE);
            rvRecipes.setVisibility(View.INVISIBLE);
        } else {
            errorView.setVisibility(View.GONE);
            rvRecipes.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showRecipeDetail(int recipeId) {
        startActivity(RecipeDetailActivity.getStartingIntent(getContext(), recipeId));
    }

    @Override
    public void onClick(RecipeModel recipeModel) {
        presenter.openRecipeDetail(recipeModel);
    }
}
