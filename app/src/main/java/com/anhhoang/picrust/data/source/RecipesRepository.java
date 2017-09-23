package com.anhhoang.picrust.data.source;

import android.support.annotation.NonNull;

import com.anhhoang.picrust.data.models.RecipeModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anh.hoang on 9/23/17.
 */

public class RecipesRepository implements BaseDataSource<RecipeModel> {
    private static RecipesRepository INSTANCE;

    private BaseDataSource<RecipeModel> recipesLocalDataSource;
    private BaseDataSource<RecipeModel> recipesRemoteDataSource;

    private Map<Integer, RecipeModel> cachedRecipes;
    private boolean forceLoad;

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
        if (cachedRecipes != null && !forceLoad) {
            callback.onLoaded(new ArrayList<>(cachedRecipes.values()));
            return;
        }

        if (!forceLoad) {
            getFromRemote(callback);
        } else {
            this.recipesLocalDataSource.get(new ResultsCallback<RecipeModel>() {
                @Override
                public void onLoaded(List<RecipeModel> result) {
                    refreshCache(result);
                    callback.onLoaded(result);
                }

                @Override
                public void onDataNotAvailable() {
                    getFromRemote(callback);
                }
            });
        }
    }

    @Override
    public void get(int id, ResultCallback<RecipeModel> callback) {
        this.recipesLocalDataSource.get(id, callback);
    }

    @Override
    public void save(Collection<RecipeModel> entities) {
        throw new UnsupportedOperationException("RecipesRepository does not support such operation.");
    }

    @Override
    public void refresh() {
        this.forceLoad = true;
    }

    private void getFromRemote(final ResultsCallback<RecipeModel> callback) {
        recipesRemoteDataSource.get(new ResultsCallback<RecipeModel>() {
            @Override
            public void onLoaded(List<RecipeModel> result) {
                if (result != null && result.size() > 0) {
                    refreshCache(result);
                    recipesLocalDataSource.save(result);
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

    private void refreshCache(List<RecipeModel> recipes) {
        if (cachedRecipes == null) {
            cachedRecipes = new LinkedHashMap<>();
        }
        cachedRecipes.clear();

        for (RecipeModel recipe : recipes) {
            cachedRecipes.put(recipe.recipe.getId(), recipe);
        }
    }
}
