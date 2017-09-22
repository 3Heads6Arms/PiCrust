package com.anhhoang.picrust.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by anh.hoang on 9/22/17.
 */

@Entity(
        tableName = "steps",
        primaryKeys = {"id", "recipeId"},
        foreignKeys = @ForeignKey(
                entity = Recipe.class,
                parentColumns = "id",
                childColumns = "recipeId",
                onDelete = ForeignKey.CASCADE
        )
)
public class Step {
    private final int id;
    private final String shortDescription;
    private final String description;
    @Nullable
    private final String videoURL;
    @Nullable
    private final String thumbnailURL;

    private final int recipeId;

    public Step(int id, @NonNull String shortDescription, @NonNull String description, @Nullable String videoURL, @Nullable String thumbnailURL, int recipeId) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
        this.recipeId = recipeId;
    }

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    @Nullable
    public String getVideoURL() {
        return videoURL;
    }

    @Nullable
    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public int getRecipeId() {
        return recipeId;
    }
}
