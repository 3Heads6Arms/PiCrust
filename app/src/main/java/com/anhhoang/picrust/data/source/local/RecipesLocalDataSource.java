package com.anhhoang.picrust.data.source.local;

import com.anhhoang.picrust.data.Ingredient;
import com.anhhoang.picrust.data.Step;
import com.anhhoang.picrust.data.models.RecipeModel;
import com.anhhoang.picrust.data.source.BaseDataSource;
import com.anhhoang.picrust.utils.AppExecutor;

import java.util.List;

/**
 * Created by anh.hoang on 9/23/17.
 */

public class RecipesLocalDataSource implements BaseDataSource<RecipeModel> {
    private static RecipesLocalDataSource INSTANCE;

    private final PiCrustDatabase piCrustDatabase;
    private final AppExecutor executor;

    private RecipesLocalDataSource(AppExecutor executor, PiCrustDatabase piCrustDatabase) {
        this.executor = executor;
        this.piCrustDatabase = piCrustDatabase;
    }

    public static RecipesLocalDataSource getInstance(AppExecutor executor, PiCrustDatabase piCrustDatabase) {
        if (INSTANCE == null) {
            INSTANCE = new RecipesLocalDataSource(executor, piCrustDatabase);
        }

        return INSTANCE;
    }

    @Override
    public void get(final ResultsCallback<RecipeModel> callback) {
        executor.diskIO()
                .execute(new Runnable() {
                    @Override
                    public void run() {
                        final List<RecipeModel> recipes = piCrustDatabase.recipesDao().getAll();

                        executor.mainThread()
                                .execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (recipes != null && recipes.size() > 0) {
                                            callback.onLoaded(recipes);
                                        } else {
                                            callback.onDataNotAvailable();
                                        }
                                    }
                                });
                    }
                });

    }

    @Override
    public void get(final int id, final ResultCallback<RecipeModel> callback) {
        executor.diskIO()
                .execute(new Runnable() {
                    @Override
                    public void run() {
                        final RecipeModel recipe = piCrustDatabase.recipesDao().get(id);

                        executor.mainThread()
                                .execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (recipe != null) {
                                            callback.onLoaded(recipe);
                                        } else {
                                            callback.onDataNotAvailable();
                                        }
                                    }
                                });
                    }
                });
    }

    /**
     * Insert Recipe, its required ingredients and steps to DB
     *
     * @param entities
     */
    @Override
    public void save(RecipeModel... entities) {
        for (RecipeModel model : entities) {
            piCrustDatabase.recipesDao().insert(model.recipe);

            for (Step step : model.steps) {
                step.setRecipeId(model.recipe.getId());
            }
            piCrustDatabase.stepsDao().insert(model.steps.toArray(new Step[model.steps.size()]));

            for (Ingredient ingredient : model.ingredients) {
                ingredient.setRecipeId(model.recipe.getId());
            }
            piCrustDatabase.ingredientsDao().insert(model.ingredients.toArray(new Ingredient[model.ingredients.size()]));
        }
    }
}
