package com.anhhoang.picrust.data.source;

import android.support.annotation.NonNull;

import com.anhhoang.picrust.data.models.RecipeModel;

import java.util.List;

/**
 * Created by anh.hoang on 9/23/17.
 */

public class RecipesRepository implements BaseDataSource<RecipeModel> {
    private static RecipesRepository INSTANCE;

    private BaseDataSource<RecipeModel> recipesLocalDataSource;
    private BaseDataSource<RecipeModel> recipesRemoteDataSource;

    private RecipesRepository(@NonNull BaseDataSource<RecipeModel> recipesLocalDataSource,
                              @NonNull BaseDataSource<RecipeModel> recipesRemoteDataSource) {
        this.recipesLocalDataSource = recipesLocalDataSource;
        this.recipesRemoteDataSource = recipesRemoteDataSource;
    }

    public static RecipesRepository getInstance(@NonNull BaseDataSource<RecipeModel> recipesLocalDataSource,
                                                @NonNull BaseDataSource<RecipeModel> recipesRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RecipesRepository(recipesLocalDataSource, recipesRemoteDataSource);
        }

        return INSTANCE;
    }

    @Override
    public void get(final ResultsCallback<RecipeModel> callback) {
        this.recipesLocalDataSource.get(new ResultsCallback<RecipeModel>() {
            @Override
            public void onLoaded(List<RecipeModel> result) {
                callback.onLoaded(result);
            }

            @Override
            public void onDataNotAvailable() {
                recipesRemoteDataSource.get(new ResultsCallback<RecipeModel>() {
                    @Override
                    public void onLoaded(List<RecipeModel> result) {
                        if (result != null && result.size() > 0) {
                            recipesLocalDataSource.save(result.toArray(new RecipeModel[result.size()]));
                            callback.onLoaded(result);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });

    }

    @Override
    public void get(int id, ResultCallback<RecipeModel> callback) {
        this.recipesLocalDataSource.get(id, callback);
    }

    @Override
    public void save(RecipeModel... entities) {
        throw new UnsupportedOperationException("RecipesRepository does not support such operation.");
    }
}
