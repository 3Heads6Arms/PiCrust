package com.anhhoang.picrust.widgets;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class UpdateWidgetReceiver extends BroadcastReceiver {
    private static final String UPDATE_WIDGET_ACTION = "com.anhhoang.picrust.UPDATE_WIDGET";

    public static Intent getStartingBroadcastIntent(Context context) {
        Intent intent = new Intent(context, UpdateWidgetReceiver.class);
        intent.setAction(UPDATE_WIDGET_ACTION);

        return intent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (UPDATE_WIDGET_ACTION.equals(intent.getAction())) {
            AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
            int[] ids = widgetManager.getAppWidgetIds(new ComponentName(context, PiCrustWidget.class));

            Intent updateWidget = new Intent(context.getApplicationContext(), PiCrustWidget.class);
            updateWidget.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            updateWidget.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);

            context.sendBroadcast(updateWidget);
        }
    }
}
