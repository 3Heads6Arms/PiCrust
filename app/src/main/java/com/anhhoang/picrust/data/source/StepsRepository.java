package com.anhhoang.picrust.data.source;

import com.anhhoang.picrust.data.Step;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anh.hoang on 9/24/17.
 */

public class StepsRepository implements BaseDataSource<Step> {
    private static StepsRepository INSTANCE;

    private final BaseDataSource<Step> dataSource;

    private int recipeId;
    private Map<Integer, Step> cachedSteps;

    private StepsRepository(BaseDataSource<Step> dataSource, int recipeId) {
        this.dataSource = dataSource;
        this.recipeId = recipeId;
    }

    public static StepsRepository getInstance(BaseDataSource<Step> dataSource, int recipeId) {
        if (INSTANCE == null) {
            INSTANCE = new StepsRepository(dataSource, recipeId);
        }

        return INSTANCE;
    }

    @Override
    public void get(final ResultsCallback<Step> callback) {
        if (cachedSteps != null && isFromCurrentRecipe(recipeId)) {
            callback.onLoaded(new ArrayList<>(cachedSteps.values()));
        } else {
            dataSource.get(new ResultsCallback<Step>() {
                @Override
                public void onLoaded(List<Step> result) {
                    refreshCache(result);

                    if (result != null && result.size() > 0) {
                        callback.onLoaded(result);
                    } else {
                        callback.onDataNotAvailable(null);
                    }
                }

                @Override
                public void onDataNotAvailable(Object additionalInfo) {
                    callback.onDataNotAvailable(null);
                }
            });
        }
    }

    @Override
    public void get(int id, ResultCallback<Step> callback) {
        if (cachedSteps != null && isFromCurrentRecipe(recipeId) && cachedSteps.containsKey(id)) {
            callback.onLoaded(cachedSteps.get(id));
        } else {
            dataSource.get(id, callback);
        }
    }

    @Override
    public void save(Collection<Step> entities) {
        // StepsRepository is not required to implement such function
    }

    @Override
    public void refresh() {
        // StepsRepository is not required to implement such function
    }

    /**
     * Check if cached steps is about given recipe
     *
     * @param recipeId - Id of the recipe to check against
     * @return
     */
    private boolean isFromCurrentRecipe(int recipeId) {
        if (cachedSteps == null) {
            return false;
        }

        // all steps should be for the same recipe.
        for (Step step : cachedSteps.values()) {
            if (step.getRecipeId() != recipeId) {
                return false;
            }
        }
        return true;
    }

    private void refreshCache(List<Step> steps) {
        if (cachedSteps == null) {
            cachedSteps = new LinkedHashMap<>();
        }

        cachedSteps.clear();
        if (steps != null) {
            for (Step step : steps) {
                cachedSteps.put(step.getId(), step);
            }
        }
    }
}
