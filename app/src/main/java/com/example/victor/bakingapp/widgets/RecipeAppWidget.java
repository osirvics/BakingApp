package com.example.victor.bakingapp.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.victor.bakingapp.MainActivity;
import com.example.victor.bakingapp.R;
import com.example.victor.bakingapp.model.Ingredient;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, ArrayList<Ingredient> ingredients, String recipeName) {


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredient_list_item);
        views.setTextViewText(R.id.widget_recipe_name, recipeName);
        views.removeAllViews(R.id.widget_ingredients_container);
       populateIngredients(views, ingredients, context);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.full_view, pendingIntent);

       // Log.e("WidgetProvider", "Starting  actual update, ingredient size: "+ ingredients.size() );
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context,appWidgetManager, appWidgetIds);
        // There may be multiple widgets active, so update all of them
//        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId);
//        }
       // Log.e("WidgetProvider", "On update called");
        //RecipeService.startAction(context);
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
                                           int[] appWidgetIds, ArrayList<Ingredient> ingredients, String recipeName) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId,ingredients ,recipeName);
            //Log.e("WidgetProvider", "Starting update loop, ingredient size: "+ ingredients.size() );
        }
    }

    private static void populateIngredients(RemoteViews containerView, ArrayList<Ingredient> ingredients, Context context) {
        for (Ingredient ingredient : ingredients) {
            float quantity;
            String measure;
            String actualIngredient;
            RemoteViews mIngredientView = new RemoteViews(context.getPackageName(),   R.layout.widget_ingredient_item);
            quantity = ingredient.getQuantity();
            measure = ingredient.getMeasure();
            actualIngredient = ingredient.getIngredient();
            String fullDescription = String.valueOf(quantity) + " " + measure + " " + actualIngredient + "\n";
            mIngredientView.setTextViewText(R.id.app_widget_ingredient_name, fullDescription);
            containerView.addView(R.id.widget_ingredients_container, mIngredientView);

        }
    }

    @Override
    public void onEnabled(Context context) {
        RecipeService.startAction(context);
        super.onEnabled(context);
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        //RecipeService.startAction(context);
    }
}

