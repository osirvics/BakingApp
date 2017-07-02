package com.example.victor.bakingapp.widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.example.victor.bakingapp.Utils;
import com.example.victor.bakingapp.data.RecipeContract;
import com.example.victor.bakingapp.model.Ingredient;

import java.util.ArrayList;

/**
 * Created by Victor on 01/07/2017.
 */

public class RecipeService extends IntentService {

    ArrayList<Ingredient> ingredients;
    public RecipeService() {
        super("RecipeService");
    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context, RecipeService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int id = Utils.getFavorieRecipe(this);
        String rowId = String.valueOf(id);
        String selectionClause = RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + " LIKE ?";
        String[] selectionArgs = {rowId};
        try {


            Cursor mIngredientcursor = getContentResolver().query(RecipeContract.RecipeEntry.INGREDIENTS_CONTENT_URI,
                    null,
                    selectionClause,
                    selectionArgs, RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + " ASC");
            ingredients = new ArrayList<>();

            if(mIngredientcursor != null && mIngredientcursor.moveToFirst()) {
                int quantityIndex = mIngredientcursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_INGRDIENT_QUANTITY);
                int measureIndex = mIngredientcursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_INGRDIENT_MEASURE);
                int ingrdientIndex = mIngredientcursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_INGRDIENT_NAME);
                do {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setQuantity(mIngredientcursor.getLong(quantityIndex));
                    ingredient.setMeasure(mIngredientcursor.getString(measureIndex));
                    ingredient.setIngredient(mIngredientcursor.getString(ingrdientIndex));
                    ingredients.add(ingredient);
                }
                while (mIngredientcursor.moveToNext());
                mIngredientcursor.close();

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeAppWidget.class));
                String recipeName = Utils.getRecipeName(id, this);
                RecipeAppWidget.updateRecipeWidgets(getApplicationContext(),appWidgetManager,appWidgetIds,ingredients,recipeName);
            }


            } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
