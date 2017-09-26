package com.anhhoang.picrust.ui.ingredients;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.anhhoang.picrust.R;
import com.anhhoang.picrust.data.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientsActivity extends AppCompatActivity {
    private static final String EXTRA_INGREDIENTS = "ExtraIngredients";

    public static Intent getStartingIntent(Context context, List<Ingredient> ingredients) {
        Intent intent = new Intent(context, IngredientsActivity.class);
        intent.putExtra(EXTRA_INGREDIENTS, new ArrayList<>(ingredients));

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
        if (!intent.hasExtra(EXTRA_INGREDIENTS)) {
            throw new IllegalArgumentException("Activity started without required intent extra - EXTRA_INGREDIENTS");
        }
        List<Ingredient> ingredients = intent.getParcelableArrayListExtra(EXTRA_INGREDIENTS);

        Log.d(IngredientsActivity.class.getSimpleName(), ingredients.size() + "");
    }
}
