package com.anhhoang.picrust.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by anh.hoang on 9/22/17.
 */

@Entity(
        tableName = "ingredients",
        foreignKeys = @ForeignKey(
                entity = Recipe.class,
                parentColumns = "id",
                childColumns = "recipeId",
                onDelete = ForeignKey.CASCADE
        )
)
public class Ingredient {
    @PrimaryKey(autoGenerate = true)
    private final int id;
    private final double quantity;
    private final String measure;
    private final String ingredient;
    @ForeignKey(
            entity = Recipe.class,
            parentColumns = {"id"},
            childColumns = {"recipeId"},
            onDelete = ForeignKey.CASCADE)
    private int recipeId;

    @Ignore
    public Ingredient(double quantity, String measure, String ingredient, int recipeId) {
        this(0, quantity, measure, ingredient, recipeId);
    }

    public Ingredient(int id, double quantity, String measure, String ingredient, int recipeId) {
        this.id = id;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
        this.recipeId = recipeId;
    }

    public int getId() {
        return id;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
}
