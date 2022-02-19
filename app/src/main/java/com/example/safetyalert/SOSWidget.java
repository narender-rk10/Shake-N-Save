package com.example.safetyalert;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class SOSWidget extends AppWidgetProvider {
    public static String TOGGLE_WINET = "ServiceMine";
    private static boolean serviceRunning = false;
    private static Intent serviceIntent;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.s_o_s_widget);
//        views.setTextViewText(R.id.appwidget_text, widgetText);

        Intent newIntent = new Intent(context, SOSWidget.class);
        newIntent.setAction("start");
        newIntent.setAction(TOGGLE_WINET);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, newIntent, 0);
        views.setOnClickPendingIntent(R.id.sos_widget, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(TOGGLE_WINET)) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.s_o_s_widget);

            // Create a fresh intent
            Intent serviceIntent = new Intent(context, ServiceMine.class);
            serviceIntent.setAction("start");
            if(serviceRunning) {
                context.stopService(serviceIntent);
                remoteViews.setViewVisibility(R.id.sos_widget, View.VISIBLE);
                Toast.makeText(context, "serviceStopped", Toast.LENGTH_SHORT).show();
            } else {
                context.startService(serviceIntent);
                remoteViews.setViewVisibility(R.id.sos_widget, View.VISIBLE);
                Toast.makeText(context, "serviceStarted", Toast.LENGTH_SHORT).show();
            }
            serviceRunning=!serviceRunning;
            ComponentName componentName = new ComponentName(context, SOSWidget.class);
            AppWidgetManager.getInstance(context).updateAppWidget(componentName, remoteViews);
        }
        super.onReceive(context, intent);
    }

}