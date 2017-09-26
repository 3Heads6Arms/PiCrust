package com.anhhoang.picrust.ui.recipes;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.anhhoang.picrust.Injection;
import com.anhhoang.picrust.R;
import com.anhhoang.picrust.data.models.RecipeModel;
import com.anhhoang.picrust.ui.recipedetails.RecipeDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesActivity extends AppCompatActivity implements RecipesContracts.View, RecipesAdapter.OnClickListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Inject Presenter into Activity
        new RecipesPresenter(this, Injection.provideRecipesRepository(getApplicationContext()));


        Configuration configuration = getResources().getConfiguration();

        if (recipesAdapter == null) { // fragment instance is retained, may not required to create new adapter
            recipesAdapter = new RecipesAdapter(null, R.layout.recipe_item_view, this);
        }

        rvRecipes.setAdapter(recipesAdapter);
        rvRecipes.setHasFixedSize(true);
        // Set displaying option
        if (configuration.smallestScreenWidthDp < 600) {
            rvRecipes.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        } else {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) rvRecipes.getLayoutManager();

            if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                gridLayoutManager.setSpanCount(3);
            } else {
                gridLayoutManager.setSpanCount(4);
            }
        }
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
            int errorTextRes;
            int errorDescriptionTextRes;

            switch (errorType) {
                case NETWORK:
                    errorTextRes = R.string.error_no_internet;
                    errorDescriptionTextRes = R.string.error_no_internet_description;
                    break;
                case OTHER:
                    errorTextRes = R.string.error_other;
                    errorDescriptionTextRes = R.string.error_other_description;
                    break;
                case EMPTY:
                default:
                    errorTextRes = R.string.empty_recipes_list;
                    errorDescriptionTextRes = R.string.empty_recipes_list_description;
                    break;
            }

            tvError.setText(errorTextRes);
            tvErrorDescription.setText(errorDescriptionTextRes);

            errorView.setVisibility(View.VISIBLE);
            rvRecipes.setVisibility(View.INVISIBLE);
        } else {
            errorView.setVisibility(View.GONE);
            rvRecipes.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showRecipeDetail(int recipeId) {
        startActivity(RecipeDetailActivity.getStartingIntent(this, recipeId));
    }

    @Override
    public void onClick(RecipeModel recipeModel) {
        presenter.openRecipeDetail(recipeModel);
    }
}
