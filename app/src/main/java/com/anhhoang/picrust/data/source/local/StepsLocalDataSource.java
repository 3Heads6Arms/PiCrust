package com.anhhoang.picrust.data.source.local;

import com.anhhoang.picrust.data.Step;
import com.anhhoang.picrust.data.source.BaseDataSource;
import com.anhhoang.picrust.utils.AppExecutor;

import java.util.Collection;
import java.util.List;

/**
 * Created by anh.hoang on 9/24/17.
 */

public class StepsLocalDataSource implements BaseDataSource<Step> {
    private static StepsLocalDataSource INSTANCE;

    private final AppExecutor executor;
    private final StepsDao stepsDao;
    private final int recipeId;

    private StepsLocalDataSource(AppExecutor executor, StepsDao stepsDao, int recipeId) {
        this.executor = executor;
        this.stepsDao = stepsDao;
        this.recipeId = recipeId;
    }

    public static StepsLocalDataSource getInstance(AppExecutor executor, StepsDao stepsDao, int recipeId) {
        if (INSTANCE == null) {
            INSTANCE = new StepsLocalDataSource(executor, stepsDao, recipeId);
        }

        return INSTANCE;
    }

    @Override
    public void get(final ResultsCallback<Step> callback) {
        executor.diskIO()
                .execute(new Runnable() {
                    @Override
                    public void run() {
                        final List<Step> steps = stepsDao.getAll(recipeId);

                        executor.mainThread()
                                .execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (steps != null && steps.size() > 0) {
                                            callback.onLoaded(steps);
                                        } else {
                                            callback.onDataNotAvailable(null);
                                        }
                                    }
                                });
                    }
                });
    }

    @Override
    public void get(final int id, final ResultCallback<Step> callback) {
        executor.diskIO()
                .execute(new Runnable() {
                    @Override
                    public void run() {
                        final Step step = stepsDao.get(recipeId, id);

                        executor.mainThread()
                                .execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (step != null) {
                                            callback.onLoaded(step);
                                        } else {
                                            callback.onDataNotAvailable(null);
                                        }
                                    }
                                });
                    }
                });
    }

    @Override
    public void save(Collection<Step> entities) {
        // StepsLocalDataSource is not called for direct insert
    }

    @Override
    public void refresh() {
        // StepsLocalDataSource does not required to have this function
    }
}
