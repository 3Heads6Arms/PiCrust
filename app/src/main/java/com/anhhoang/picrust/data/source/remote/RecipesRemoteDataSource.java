package com.anhhoang.picrust.data.source.remote;

import com.anhhoang.picrust.data.Recipe;
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

public class RecipesRemoteDataSource implements BaseDataSource<Recipe> {
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
    public void get(final ResultsCallback<Recipe> callback) {
        this.piCrustApiService
                .getRecipes()
                .enqueue(new Callback<List<RecipeRequest>>() {
                    @Override
                    public void onResponse(Call<List<RecipeRequest>> call, Response<List<RecipeRequest>> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            List<RecipeRequest> recipeRequests = response.body();

                            List<Recipe> recipes = new ArrayList<Recipe>();
                            // Change request object into object that DB uses for its operations
                            for (RecipeRequest recipeRequest : recipeRequests) {
                                Recipe recipe = new Recipe();
                                recipe = new Recipe(
                                        recipeRequest.getId(),
                                        recipeRequest.getName(),
                                        recipeRequest.getServings(),
                                        recipeRequest.getImage());
                                recipe.setIngredients(recipeRequest.getIngredients());
                                recipe.setSteps(recipeRequest.getSteps());

                                recipes.add(recipe);
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
    public void get(long id, ResultCallback<Recipe> callback) {
        throw new UnsupportedOperationException(RecipesRemoteDataSource.class.getSimpleName() + " does not support such operation.");
    }

    @Override
    public void refresh() {
        throw new UnsupportedOperationException(RecipesRemoteDataSource.class.getSimpleName() + " does not support such operation.");
    }

    @Override
    public void save(Collection<Recipe> entities) {
        throw new UnsupportedOperationException(RecipesRemoteDataSource.class.getSimpleName() + " does not support such operation.");
    }
}
