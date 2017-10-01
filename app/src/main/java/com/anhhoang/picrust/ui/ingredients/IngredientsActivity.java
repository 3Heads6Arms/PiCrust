package com.anhhoang.picrust.ui.ingredients;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.anhhoang.picrust.R;
import com.anhhoang.picrust.data.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientsActivity extends AppCompatActivity {
    public static final String EXTRA_INGREDIENTS = "ExtraIngredients";
    private static final String EXTRA_RECIPE_NAME = "ExtraRecipeName";

    public static Intent getStartingIntent(Context context, List<Ingredient> ingredients, String recipeName) {
        Intent intent = new Intent(context, IngredientsActivity.class);
        intent.putExtra(EXTRA_INGREDIENTS, new ArrayList<>(ingredients));
        intent.putExtra(EXTRA_RECIPE_NAME, recipeName);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (!intent.hasExtra(EXTRA_INGREDIENTS) || !intent.hasExtra(EXTRA_RECIPE_NAME)) {
            throw new IllegalArgumentException("Activity started without required intent extra - EXTRA_INGREDIENTS or EXTRA_RECIPE_NAME");
        }
        getSupportActionBar().setTitle(intent.getStringExtra(EXTRA_RECIPE_NAME));

        List<Ingredient> ingredients = intent.getParcelableArrayListExtra(EXTRA_INGREDIENTS);

        FragmentManager fragmentManager = getSupportFragmentManager();
        IngredientsFragment ingredientsFragment = (IngredientsFragment) fragmentManager.findFragmentById(R.id.ingredients_fragment);

        new IngredientsPresenter(ingredientsFragment, ingredients);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
