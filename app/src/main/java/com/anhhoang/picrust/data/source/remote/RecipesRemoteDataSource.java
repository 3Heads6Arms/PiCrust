package com.anhhoang.picrust.data.source.remote;

import com.anhhoang.picrust.data.Recipe;
import com.anhhoang.picrust.data.models.RecipeModel;
import com.anhhoang.picrust.data.models.RecipeRequest;
import com.anhhoang.picrust.data.network.PiCrustApiService;
import com.anhhoang.picrust.data.source.BaseDataSource;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anh.hoang on 9/23/17.
 */

public class RecipesRemoteDataSource implements BaseDataSource<RecipeModel> {
    private static RecipesRemoteDataSource INSTANCE;

    private final PiCrustApiService piCrustApiService;

    private RecipesRemoteDataSource(PiCrustApiService piCrustApiService) {
        this.piCrustApiService = piCrustApiService;
    }

    public static RecipesRemoteDataSource getIsntance(PiCrustApiService piCrustApiService) {
        if (INSTANCE == null) {
            INSTANCE = new RecipesRemoteDataSource(piCrustApiService);
        }

        return INSTANCE;
    }

    /**
     * Get recipes from server triggers callback accordingly
     *
     * @param callback
     */
    @Override
    public void get(final ResultsCallback<RecipeModel> callback) {
        this.piCrustApiService
                .getRecipes()
                .enqueue(new Callback<List<RecipeRequest>>() {
                    @Override
                    public void onResponse(Call<List<RecipeRequest>> call, Response<List<RecipeRequest>> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            List<RecipeRequest> recipeRequests = response.body();

                            List<RecipeModel> recipes = new ArrayList<RecipeModel>();
                            // Change request object into object that DB uses for its operations
                            for (RecipeRequest recipeRequest : recipeRequests) {
                                RecipeModel recipeModel = new RecipeModel();
                                recipeModel.recipe = new Recipe(
                                        recipeRequest.getId(),
                                        recipeRequest.getName(),
                                        recipeRequest.getServings(),
                                        recipeRequest.getImage());
                                recipeModel.ingredients = recipeRequest.getIngredients();
                                recipeModel.steps = recipeRequest.getSteps();

                                recipes.add(recipeModel);
                            }

                            callback.onLoaded(recipes);
                        } else {
                            callback.onDataNotAvailable(response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<RecipeRequest>> call, Throwable t) {
                        callback.onDataNotAvailable(t);
                    }
                });
    }

    @Override
    public void get(int id, ResultCallback<RecipeModel> callback) {
        throw new UnsupportedOperationException(RecipesRemoteDataSource.class.getSimpleName() + " does not support such operation.");
    }

    @Override
    public void refresh() {
        throw new UnsupportedOperationException(RecipesRemoteDataSource.class.getSimpleName() + " does not support such operation.");
    }

    @Override
    public void save(Collection<RecipeModel> entities) {
        throw new UnsupportedOperationException(RecipesRemoteDataSource.class.getSimpleName() + " does not support such operation.");
    }
}
