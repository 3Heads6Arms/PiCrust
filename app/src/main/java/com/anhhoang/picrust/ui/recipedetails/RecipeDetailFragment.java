package com.anhhoang.picrust.ui.recipedetails;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anhhoang.picrust.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeDetailFragment extends Fragment {

    public RecipeDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false);
    }
}
