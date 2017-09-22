package com.anhhoang.picrust.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

/**
 * Created by anh.hoang on 9/22/17.
 */

@Entity(tableName = "recipes")
public class Recipe {
    @PrimaryKey
    private final int id;
    private final String name;
    private final int servings;
    @Nullable
    private final String image;

    public Recipe(int id, String name, int servings, @Nullable String image) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }
}
