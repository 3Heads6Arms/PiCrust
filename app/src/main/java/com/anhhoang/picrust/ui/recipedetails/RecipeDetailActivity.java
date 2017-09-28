package com.anhhoang.picrust.ui.recipedetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.anhhoang.picrust.Injection;
import com.anhhoang.picrust.R;
import com.anhhoang.picrust.data.Ingredient;
import com.anhhoang.picrust.data.Step;
import com.anhhoang.picrust.data.models.RecipeItem;
import com.anhhoang.picrust.data.models.RecipeModel;
import com.anhhoang.picrust.ui.ingredients.IngredientsActivity;
import com.anhhoang.picrust.ui.step.StepActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailContracts.View, RecipeDetailAdapter.OnItemClickListener {
    public static final String EXTRA_RECIPE_ID = "ExtraRecipeId";


    @BindView(R.id.recipe_detail_recycler_view)
    RecyclerView rvRecipeDetail;
    @BindView(R.id.error_view)
    View errorView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private RecipeDetailContracts.Presenter presenter;
    private RecipeDetailAdapter recipeDetailAdapter;

    public static Intent getStartingIntent(Context context, int recipeId) {
        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (!intent.hasExtra(EXTRA_RECIPE_ID)) {
            throw new IllegalArgumentException("Activity started without required intent extra EXTRA_RECIPE_ID");
        }

        int recipeId = intent.getIntExtra(EXTRA_RECIPE_ID, 0);

        // Inject Presenter into activity
        new RecipeDetailPresenter(
                this,
                Injection.provideRecipesRepository(getApplicationContext()),
                recipeId);

        recipeDetailAdapter = new RecipeDetailAdapter(null, this);
        rvRecipeDetail.setAdapter(recipeDetailAdapter);
        rvRecipeDetail.setHasFixedSize(true);
        // TODO: TwoPane handle
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(RecipeDetailContracts.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showIngredients(List<Ingredient> ingredients) {
        // TODO: TwoPane handle
        startActivity(IngredientsActivity.getStartingIntent(this, ingredients));
    }

    @Override
    public void showStep(int stepId, List<Step> steps) {
        // TODO: TwoPane handle
        startActivity(StepActivity.getStartingIntent(this, steps));
    }

    @Override
    public void showDetail(RecipeModel recipeModel) {
        recipeDetailAdapter.setRecipeModel(recipeModel);
    }

    @Override
    public void showLoadingIndicator(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            rvRecipeDetail.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            rvRecipeDetail.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showError(boolean hasError) {
        if (hasError) {
            errorView.setVisibility(View.VISIBLE);
            rvRecipeDetail.setVisibility(View.INVISIBLE);
        } else {
            errorView.setVisibility(View.INVISIBLE);
            rvRecipeDetail.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(int stepId, List<RecipeItem> items, Class tClass) {
        presenter.openStepDetail(stepId, items, tClass);
    }
}
