package com.anhhoang.picrust.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RemoteViews;

import com.anhhoang.picrust.R;
import com.anhhoang.picrust.ui.recipedetails.RecipeDetailActivity;

/**
 * Implementation of App Widget functionality.
 */
public class PiCrustWidget extends AppWidgetProvider {
    public static final int INGREDIENTS_REQUEST_CODE = 42;
    public static final String EXTRA_OPEN_INGREDIENTS = "ExtraOpenIngredients";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int recipeId = preferences.getInt(context.getString(R.string.last_accessed_recipe_key), -1);


        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.pi_crust_widget);

        if (recipeId != -1) {
            Intent remoteViewIntent = new Intent(context, PiCrustRemoteViewsService.class);
            views.setRemoteAdapter(R.id.ingredients_list_view, remoteViewIntent);

            Intent intent = RecipeDetailActivity.getStartingIntent(context, recipeId);
            intent.putExtra(EXTRA_OPEN_INGREDIENTS, INGREDIENTS_REQUEST_CODE);
            views.setOnClickPendingIntent(
                    R.id.text_view_app_name,
                    PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        } else {
            views.setViewVisibility(R.id.ingredients_list_view, View.INVISIBLE);
            views.setViewVisibility(R.id.error_text_view, View.VISIBLE);
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}

