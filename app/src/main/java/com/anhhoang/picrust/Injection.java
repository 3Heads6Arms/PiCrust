package com.anhhoang.picrust;

import android.content.Context;

import com.anhhoang.picrust.data.network.PiCrustApiService;
import com.anhhoang.picrust.data.source.RecipesRepository;
import com.anhhoang.picrust.data.source.local.PiCrustDatabase;
import com.anhhoang.picrust.data.source.local.RecipesLocalDataSource;
import com.anhhoang.picrust.data.source.remote.RecipesRemoteDataSource;
import com.anhhoang.picrust.utils.AppExecutor;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by anh.hoang on 9/23/17.
 */

public class Injection {

    public static RecipesRepository provideRecipesRepository(Context context) {
        checkNotNull(context, "context cannot be null!");

        PiCrustDatabase piCrustDatabase = PiCrustDatabase.getInstance(context);
        RecipesRepository recipesRepository = RecipesRepository.getInstance(
                RecipesLocalDataSource.getInstance(new AppExecutor(), piCrustDatabase),
                RecipesRemoteDataSource.getIsntance(PiCrustApiService.getInstance())
        );

        return recipesRepository;
    }
}
