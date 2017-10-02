package com.anhhoang.picrust.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.anhhoang.picrust.data.models.RecipeItem;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by anh.hoang on 9/22/17.
 */

@Entity
public class Ingredient implements RecipeItem, Parcelable {
    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    private long id;
    @NotNull
    private double quantity;
    @NotNull
    private String measure;
    @NotNull
    private String ingredient;

    private long recipeId;

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

    protected Ingredient(Parcel in) {
        this.id = in.readLong();
        this.quantity = in.readDouble();
        this.measure = in.readString();
        this.ingredient = in.readString();
        this.recipeId = in.readLong();
    }

    @Generated(hash = 1377901256)
    public Ingredient(long id, double quantity, @NotNull String measure, @NotNull String ingredient,
            long recipeId) {
        this.id = id;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
        this.recipeId = recipeId;
    }

    @Generated(hash = 1584798654)
    public Ingredient() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeDouble(this.quantity);
        dest.writeString(this.measure);
        dest.writeString(this.ingredient);
        dest.writeLong(this.recipeId);
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getQuantity() {
        return this.quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return this.measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return this.ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public long getRecipeId() {
        return this.recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }
}
