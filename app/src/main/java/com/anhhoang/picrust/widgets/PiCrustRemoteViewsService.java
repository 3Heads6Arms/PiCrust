package com.anhhoang.picrust.widgets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.anhhoang.picrust.PiCrustApplication;
import com.anhhoang.picrust.R;
import com.anhhoang.picrust.data.Ingredient;
import com.anhhoang.picrust.data.Recipe;

import java.util.List;

/**
 * Created by anh.hoang on 10/2/17.
 */

public class PiCrustRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new RemoteViewsFactory() {
            public List<Ingredient> ingredients;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                long recipeId = preferences.getLong(getApplicationContext().getString(R.string.widget_recipe_id_key), -1);
                Recipe recipe = ((PiCrustApplication) getApplication()).getDaoSession().getRecipeDao().load(recipeId);

                ingredients = recipe.getIngredients();
            }

            @Override
            public void onDestroy() {

            }

            @Override
            public int getCount() {
                return ingredients != null ? ingredients.size() : 0;
            }

            @Override
            public RemoteViews getViewAt(int i) {
                Ingredient ingredient = ingredients.get(i);

                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget_list_item);
                remoteViews.setTextViewText(R.id.item_name_text_view, ingredient.getIngredient());

                return remoteViews;
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int i) {
                return ingredients.get(i).getId();
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
