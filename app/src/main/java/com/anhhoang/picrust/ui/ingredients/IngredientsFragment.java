package com.anhhoang.picrust.ui.ingredients;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anhhoang.picrust.R;
import com.anhhoang.picrust.data.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class IngredientsFragment extends Fragment implements IngredientsContracts.View {

    @BindView(R.id.ingredients_recycler_view)
    RecyclerView rvIngredients;
    @BindView(R.id.error_view)
    View errorView;
    private IngredientsContracts.Presenter presenter;
    private IngredientsAdapter ingredientsAdapter;

    public IngredientsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);
        ButterKnife.bind(this, view);

        ingredientsAdapter = new IngredientsAdapter(null);
        rvIngredients.setAdapter(ingredientsAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setPresenter(IngredientsContracts.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showIngredients(List<Ingredient> ingredients) {
        ingredientsAdapter.setIngredients(ingredients);
    }

    @Override
    public void showError(boolean hasError) {
        if (hasError) {
            rvIngredients.setVisibility(View.INVISIBLE);
            errorView.setVisibility(View.VISIBLE);
        } else {
            rvIngredients.setVisibility(View.VISIBLE);
            errorView.setVisibility(View.INVISIBLE);
        }
    }
}
