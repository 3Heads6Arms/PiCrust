package com.anhhoang.picrust.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.anhhoang.picrust.data.Ingredient;

import java.util.List;


/**
 * Created by anh.hoang on 9/22/17.
 */

@Dao
public interface IngredientsDao {
    @Query("Select * from ingredients where recipeId = :recipeId")
    List<Ingredient> getAll(int recipeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ingredient... ingredients);
}
