package com.anhhoang.picrust.data.models;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import com.anhhoang.picrust.data.Ingredient;
import com.anhhoang.picrust.data.Recipe;
import com.anhhoang.picrust.data.Step;

import java.util.List;

/**
 * Created by anh.hoang on 9/22/17.
 */

public class RecipeModel {
    @Embedded
    public Recipe recipe;

    @Relation(entity = Ingredient.class, parentColumn = "id", entityColumn = "recipeId")
    public List<Ingredient> ingredients;
    @Relation(entity = Step.class, parentColumn = "id", entityColumn = "recipeId")
    public List<Step> steps;
}
