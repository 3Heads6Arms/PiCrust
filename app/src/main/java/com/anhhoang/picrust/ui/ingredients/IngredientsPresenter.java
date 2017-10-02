package com.anhhoang.picrust.ui.ingredients;

import android.os.Parcel;
import android.os.Parcelable;

import com.anhhoang.picrust.data.Ingredient;

import java.util.List;

/**
 * Created by anh.hoang on 9/27/17.
 */

public class IngredientsPresenter implements IngredientsContracts.Presenter, Parcelable {

    public static final Parcelable.Creator<IngredientsPresenter> CREATOR = new Parcelable.Creator<IngredientsPresenter>() {
        @Override
        public IngredientsPresenter createFromParcel(Parcel source) {
            return new IngredientsPresenter(source);
        }

        @Override
        public IngredientsPresenter[] newArray(int size) {
            return new IngredientsPresenter[size];
        }
    };
    private final List<Ingredient> ingredients;
    private IngredientsContracts.View view;

    public IngredientsPresenter(IngredientsContracts.View view, List<Ingredient> ingredients) {
        this.view = view;
        this.ingredients = ingredients;

        view.setPresenter(this);
    }

    protected IngredientsPresenter(Parcel in) {
        this.ingredients = in.createTypedArrayList(Ingredient.CREATOR);
    }

    @Override
    public void start() {
        loadIngredients();
    }

    @Override
    public void loadIngredients() {
        if (ingredients == null || ingredients.size() <= 0) {
            view.showError(true);
        } else {
            view.showError(false);
            view.showIngredients(ingredients);
        }
    }

    @Override
    public void switchView(IngredientsContracts.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.ingredients);
    }
}
