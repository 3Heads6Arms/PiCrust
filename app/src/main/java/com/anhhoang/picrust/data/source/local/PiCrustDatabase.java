package com.anhhoang.picrust.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.anhhoang.picrust.data.Ingredient;
import com.anhhoang.picrust.data.Recipe;
import com.anhhoang.picrust.data.Step;

/**
 * Created by anh.hoang on 9/22/17.
 */

@Database(
        entities = {Recipe.class, Ingredient.class, Step.class},
        version = 1,
        exportSchema = false
)
public abstract class PiCrustDatabase extends RoomDatabase {
    private static final Object dbLock = new Object();
    private static PiCrustDatabase INSTANCE;

    public static PiCrustDatabase getInstance(Context context) {
        synchronized (dbLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                        context,
                        PiCrustDatabase.class,
                        "PiCrust.db")
                        .build();
            }

            return INSTANCE;
        }
    }

    public abstract RecipesDao recipesDao();

    public abstract IngredientsDao ingredientsDao();

    public abstract StepsDao stepsDao();

}
