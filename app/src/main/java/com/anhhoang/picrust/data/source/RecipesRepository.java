package com.anhhoang.picrust.data.source;

import android.support.annotation.NonNull;

import com.anhhoang.picrust.data.Recipe;
import com.anhhoang.picrust.data.models.RecipeRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anh.hoang on 9/23/17.
 */

public class RecipesRepository implements BaseDataSource<Recipe> {
    private static RecipesRepository INSTANCE;

    private BaseDataSource<Recipe> recipesLocalDataSource;
    private BaseDataSource<Recipe> recipesRemoteDataSource;

    private Map<Long, Recipe> cachedRecipes;
    private boolean forceLoad;

    private RecipesRepository(@NonNull BaseDataSource<Recipe> recipesLocalDataSource,
                              @NonNull BaseDataSource<Recipe> recipesRemoteDataSource) {
        this.recipesLocalDataSource = recipesLocalDataSource;
        this.recipesRemoteDataSource = recipesRemoteDataSource;
    }

    public static RecipesRepository getInstance(@NonNull BaseDataSource<Recipe> recipesLocalDataSource,
                                                @NonNull BaseDataSource<Recipe> recipesRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RecipesRepository(recipesLocalDataSource, recipesRemoteDataSource);
        }

        return INSTANCE;
    }

    @Override
    public void get(final ResultsCallback<Recipe> callback) {
        if (cachedRecipes != null && !forceLoad) {
            callback.onLoaded(new ArrayList<>(cachedRecipes.values()));
            return;
        }

        if (!forceLoad) {
            getFromRemote(callback);
        } else {
            this.recipesLocalDataSource.get(new ResultsCallback<Recipe>() {
                @Override
                public void onLoaded(List<Recipe> result) {
                    refreshCache(result);
                    callback.onLoaded(result);
                }

                @Override
                public void onDataNotAvailable(Object additionalInfo) {
                    getFromRemote(callback);
                }
            });
        }
    }

    @Override
    public void get(long id, ResultCallback<Recipe> callback) {
        if (cachedRecipes != null && cachedRecipes.containsKey(id)) {
            callback.onLoaded(cachedRecipes.get(id));
            return;
        }

        this.recipesLocalDataSource.get(id, callback);
    }

    @Override
    public void save(Collection<Recipe> entities) {
        throw new UnsupportedOperationException("RecipesRepository does not support such operation.");
    }

    @Override
    public void refresh() {
        this.forceLoad = true;
    }

    private void getFromRemote(final ResultsCallback<Recipe> callback) {
        recipesRemoteDataSource.get(new ResultsCallback<Recipe>() {
            @Override
            public void onLoaded(List<Recipe> result) {
                if (result != null && result.size() > 0) {
                    refreshCache(result);
                    recipesLocalDataSource.save(result);
                    callback.onLoaded(result);
                } else {
                    callback.onDataNotAvailable(null);
                }
            }

            @Override
            public void onDataNotAvailable(Object additionalInfo) {
                callback.onDataNotAvailable(additionalInfo);
            }
        });
    }

    private void refreshCache(List<Recipe> recipes) {
        if (cachedRecipes == null) {
            cachedRecipes = new LinkedHashMap<>();
        }
        cachedRecipes.clear();

        if (recipes != null) {
            for (Recipe recipe : recipes) {
                cachedRecipes.put(recipe.getId(), recipe);
            }
        }
    }
}
