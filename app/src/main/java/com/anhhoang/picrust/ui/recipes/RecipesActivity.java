package com.anhhoang.picrust.ui.recipes;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.anhhoang.picrust.Injection;
import com.anhhoang.picrust.R;

public class RecipesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();
        RecipesFragment recipesFragment = (RecipesFragment) fragmentManager.findFragmentById(R.id.recipes_fragment);

        new RecipesPresenter(recipesFragment, Injection.provideRecipesRepository(getApplicationContext()));
    }

}
