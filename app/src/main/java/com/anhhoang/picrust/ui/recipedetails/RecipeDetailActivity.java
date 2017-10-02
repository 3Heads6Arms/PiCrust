package com.anhhoang.picrust.ui.recipedetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.anhhoang.picrust.Injection;
import com.anhhoang.picrust.PiCrustApplication;
import com.anhhoang.picrust.R;
import com.anhhoang.picrust.data.Ingredient;
import com.anhhoang.picrust.data.Recipe;
import com.anhhoang.picrust.data.Step;
import com.anhhoang.picrust.data.models.RecipeItem;
import com.anhhoang.picrust.ui.ingredients.IngredientsActivity;
import com.anhhoang.picrust.ui.ingredients.IngredientsContracts;
import com.anhhoang.picrust.ui.ingredients.IngredientsFragment;
import com.anhhoang.picrust.ui.ingredients.IngredientsPresenter;
import com.anhhoang.picrust.ui.step.StepActivity;
import com.anhhoang.picrust.ui.step.StepContracts;
import com.anhhoang.picrust.ui.step.StepFragment;
import com.anhhoang.picrust.ui.step.StepPresenter;
import com.anhhoang.picrust.widgets.PiCrustWidget;

import java.util.List;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailContracts.View, RecipeDetailAdapter.OnItemClickListener {
    public static final String EXTRA_RECIPE_ID = "ExtraRecipeId";
    private static final String TAG = "RecipeFragmentTag";
    private static final String INGREDIENTS_PRESENTER_KEY = "IngredientsPresenterKey";
    private static final String STEP_PRESENTER_KEY = "StepPresenterKey";

    @BindView(R.id.recipe_detail_recycler_view)
    RecyclerView rvRecipeDetail;
    @BindView(R.id.error_view)
    View errorView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindBool(R.bool.is_two_pane)
    boolean twoPane;

    private RecipeDetailContracts.Presenter presenter;
    private IngredientsContracts.Presenter ingredientsPresenter;
    private StepContracts.Presenter stepPresenter;
    private RecipeDetailAdapter recipeDetailAdapter;
    private boolean isFirstStart;
    private boolean shouldOpenIngredients;
    private String recipeName;

    public static Intent getStartingIntent(Context context, long recipeId) {
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

        long recipeId = intent.getLongExtra(EXTRA_RECIPE_ID, 0);
        shouldOpenIngredients = intent.getIntExtra(PiCrustWidget.EXTRA_OPEN_INGREDIENTS, -1) == PiCrustWidget.INGREDIENTS_REQUEST_CODE;

        // Inject Presenter into activity
        new RecipeDetailPresenter(
                this,
                Injection.provideRecipesRepository(((PiCrustApplication) getApplication()).getDaoSession()),
                recipeId);

        recipeDetailAdapter = new RecipeDetailAdapter(null, this);
        rvRecipeDetail.setAdapter(recipeDetailAdapter);
        rvRecipeDetail.setHasFixedSize(true);

        if (twoPane) {
            if (savedInstanceState == null) {
                isFirstStart = true;
            } else {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentByTag(TAG);
                if (fragment instanceof StepFragment) {
                    stepPresenter = savedInstanceState.getParcelable(STEP_PRESENTER_KEY);
                    stepPresenter.switchView((StepContracts.View) fragment);
                } else if (fragment instanceof IngredientsFragment) {
                    ingredientsPresenter = savedInstanceState.getParcelable(INGREDIENTS_PRESENTER_KEY);
                    ingredientsPresenter.switchView((IngredientsContracts.View) fragment);
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(INGREDIENTS_PRESENTER_KEY, (Parcelable) ingredientsPresenter);
        outState.putParcelable(STEP_PRESENTER_KEY, (Parcelable) stepPresenter);

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
        if (twoPane) {
            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            if (ingredientsPresenter == null) {
                ingredientsPresenter = new IngredientsPresenter(ingredientsFragment, ingredients);
            } else {
                ingredientsPresenter.switchView(ingredientsFragment);
            }
            changeFragment(ingredientsFragment);
        } else {
            startActivity(IngredientsActivity.getStartingIntent(this, ingredients, recipeName));
        }
    }

    @Override
    public void showStep(long stepId, List<Step> steps) {
        if (twoPane) {
            StepFragment stepFragment = new StepFragment();
            if (stepPresenter == null) {
                stepPresenter = new StepPresenter(stepFragment, stepId, steps);
            } else {
                stepPresenter.setStep(stepId);
                stepPresenter.switchView(stepFragment);
            }

            changeFragment(stepFragment);
        } else {
            startActivity(StepActivity.getStartingIntent(this, stepId, steps, recipeName));
        }
    }

    @Override
    public void showDetail(Recipe recipeModel) {
        recipeName = recipeModel.getName();
        getSupportActionBar().setTitle(recipeName);

        recipeDetailAdapter.setRecipeModel(recipeModel);

        if (isFirstStart && twoPane || shouldOpenIngredients) {
            shouldOpenIngredients = false;
            isFirstStart = false;
            showIngredients(recipeModel.getIngredients());
        }
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
    public void onClick(long stepId, List<RecipeItem> items, Class tClass) {
        presenter.openStepDetail(stepId, items, tClass);
    }

    private void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.ingredients_step_fragment, fragment, TAG)
                .commit();
    }
}
