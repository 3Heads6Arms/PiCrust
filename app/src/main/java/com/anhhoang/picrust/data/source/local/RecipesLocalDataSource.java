package com.anhhoang.picrust.data.source.local;

import com.anhhoang.picrust.data.DaoSession;
import com.anhhoang.picrust.data.Ingredient;
import com.anhhoang.picrust.data.Recipe;
import com.anhhoang.picrust.data.Step;
import com.anhhoang.picrust.data.source.BaseDataSource;
import com.anhhoang.picrust.utils.AppExecutor;

import java.util.Collection;
import java.util.List;

/**
 * Created by anh.hoang on 9/23/17.
 */

public class RecipesLocalDataSource implements BaseDataSource<Recipe> {
    private static RecipesLocalDataSource INSTANCE;

    private final DaoSession daoSession;
    private final AppExecutor executor;

    private RecipesLocalDataSource(AppExecutor executor, DaoSession daoSession) {
        this.executor = executor;
        this.daoSession = daoSession;
    }

    public static RecipesLocalDataSource getInstance(AppExecutor executor, DaoSession daoSession) {
        if (INSTANCE == null) {
            INSTANCE = new RecipesLocalDataSource(executor, daoSession);
        }

        return INSTANCE;
    }

    @Override
    public void get(final ResultsCallback<Recipe> callback) {
        executor.diskIO()
                .execute(new Runnable() {
                    @Override
                    public void run() {
                        final List<Recipe> recipes = daoSession.getRecipeDao().loadAll();

                        executor.mainThread()
                                .execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (recipes != null && recipes.size() > 0) {
                                            callback.onLoaded(recipes);
                                        } else {
                                            callback.onDataNotAvailable(null);
                                        }
                                    }
                                });
                    }
                });

    }

    @Override
    public void get(final long id, final ResultCallback<Recipe> callback) {
        executor.diskIO()
                .execute(new Runnable() {
                    @Override
                    public void run() {
                        final Recipe recipe = daoSession.getRecipeDao().load(id);

                        executor.mainThread()
                                .execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (recipe != null) {
                                            callback.onLoaded(recipe);
                                        } else {
                                            callback.onDataNotAvailable(null);
                                        }
                                    }
                                });
                    }
                });
    }

    /**
     * Insert Recipe, its required ingredients and steps to DB
     *
     * @param entities - RecipeModel, contains detail about the recipe
     */
    @Override
    public void save(final Collection<Recipe> entities) {
        executor.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                for (Recipe model : entities) {
                    daoSession.getRecipeDao().insert(model);

                    for (Step step : model.getSteps()) {
                        step.setRecipeId(model.getId());
                    }
                    daoSession.getStepDao().insertInTx(model.getSteps());

                    for (Ingredient ingredient : model.getIngredients()) {
                        ingredient.setRecipeId(model.getId());
                    }
                    daoSession.getIngredientDao().insertInTx(model.getIngredients());
                }
            }
        });
    }

    @Override
    public void refresh() {
        // DB does not require refresh
    }
}
