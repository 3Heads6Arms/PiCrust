package com.anhhoang.picrust.ui.recipedetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.anhhoang.picrust.Injection;
import com.anhhoang.picrust.R;

public class RecipeDetailActivity extends AppCompatActivity {
    public static final String EXTRA_RECIPE_ID = "ExtraRecipeId";

    public static Intent getStartingIntent(Context context, int recipeId) {
        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (!intent.hasExtra(EXTRA_RECIPE_ID)) {
            throw new IllegalArgumentException("Activity started without required intent EXTRA_RECIPE_ID");
        }

        int recipeId = intent.getIntExtra(EXTRA_RECIPE_ID, 0);

        FragmentManager fragmentManager = getSupportFragmentManager();
        RecipeDetailFragment fragment = (RecipeDetailFragment) fragmentManager.findFragmentById(R.id.recipe_detail_fragment);

        new RecipeDetailPresenter(
                fragment,
                Injection.provideRecipesRepository(getApplicationContext()),
                recipeId);
    }
}
