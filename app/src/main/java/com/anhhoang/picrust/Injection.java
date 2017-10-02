package com.anhhoang.picrust;


import com.anhhoang.picrust.data.DaoSession;
import com.anhhoang.picrust.data.network.PiCrustApiService;
import com.anhhoang.picrust.data.source.RecipesRepository;
import com.anhhoang.picrust.data.source.local.RecipesLocalDataSource;
import com.anhhoang.picrust.data.source.remote.RecipesRemoteDataSource;
import com.anhhoang.picrust.utils.AppExecutor;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by anh.hoang on 9/23/17.
 */

public class Injection {

    public static RecipesRepository provideRecipesRepository(DaoSession daoSession) {
        checkNotNull(daoSession, "daoSession cannot be null!");

        RecipesRepository recipesRepository = RecipesRepository.getInstance(
                RecipesLocalDataSource.getInstance(new AppExecutor(), daoSession),
                RecipesRemoteDataSource.getIsntance(PiCrustApiService.getInstance())
        );

        return recipesRepository;
    }
}
