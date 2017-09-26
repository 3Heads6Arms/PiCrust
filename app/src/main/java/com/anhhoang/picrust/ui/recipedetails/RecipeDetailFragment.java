package com.anhhoang.picrust.ui.recipedetails;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.anhhoang.picrust.R;
import com.anhhoang.picrust.data.Ingredient;
import com.anhhoang.picrust.data.Step;
import com.anhhoang.picrust.data.models.RecipeItem;
import com.anhhoang.picrust.data.models.RecipeModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeDetailFragment extends Fragment implements RecipeDetailContracts.View, RecipeDetailAdapter.OnItemClickListener {

    @BindView(R.id.recipe_detail_recycler_view)
    RecyclerView rvRecipeDetail;
    @BindView(R.id.error_view)
    View errorView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private RecipeDetailContracts.Presenter presenter;
    private RecipeDetailAdapter recipeDetailAdapter;

    public RecipeDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, view);

        recipeDetailAdapter = new RecipeDetailAdapter(null, this);
        rvRecipeDetail.setAdapter(recipeDetailAdapter);
        rvRecipeDetail.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setPresenter(RecipeDetailContracts.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showIngredients(List<Ingredient> ingredients) {
        // TODO:
    }

    @Override
    public void showStep(int stepId, List<Step> steps) {
        // TODO:
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
