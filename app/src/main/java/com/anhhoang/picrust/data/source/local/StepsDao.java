package com.anhhoang.picrust.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.anhhoang.picrust.data.Step;

import java.util.List;

/**
 * Created by anh.hoang on 9/22/17.
 */

@Dao
public interface StepsDao {
    @Query("select * from steps where recipeId = :recipeId")
    List<Step> getAll(int recipeId);

    @Query("select * from steps where recipeId = :recipeId and id = :stepId limit 1")
    Step get(int recipeId, int stepId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Step... steps);
}
