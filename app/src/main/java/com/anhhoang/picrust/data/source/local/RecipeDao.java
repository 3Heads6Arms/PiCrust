package com.anhhoang.picrust.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.anhhoang.picrust.data.Recipe;
import com.anhhoang.picrust.data.models.RecipeModel;

import java.util.List;

/**
 * Created by anh.hoang on 9/22/17.
 */

@Dao
public interface RecipeDao {
    @Query("SELECT * from recipes")
    List<RecipeModel> getAll();

    @Query("SELECT * from recipes where id = :id")
    RecipeModel get(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Recipe... recipes);
}
