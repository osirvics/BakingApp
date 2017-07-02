package com.example.victor.bakingapp.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.victor.bakingapp.data.RecipeContract.RecipeEntry;
import com.example.victor.bakingapp.model.Ingredient;
import com.example.victor.bakingapp.model.Recipe;
import com.example.victor.bakingapp.model.Step;
import com.example.victor.bakingapp.widgets.RecipeService;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Victor on 29/06/2017.
 */

public class PersistData {
        private static String TAG = "PersistData";



        public static ContentValues[] getRecipeContentValuesFromArray(Context context, ArrayList<Recipe> recipes)
                throws JSONException {

            ContentValues[] recipeContentValues = new ContentValues[recipes.size()];
            for (int i = 0; i < recipes.size(); i++) {
                Recipe data = recipes.get(i);

                ContentValues mRecipeValues = new ContentValues();
                mRecipeValues.put(RecipeEntry.COLUMN_RECIPE_ID, data.getId());
                mRecipeValues.put(RecipeEntry.COLUMN_RECIPE_NAME, data.getName());
                mRecipeValues.put(RecipeEntry.COLUMN_RECIPE_SERVINGS, data.getServings());
                recipeContentValues[i] = mRecipeValues;
            }
            return recipeContentValues;
        }

        public static synchronized void cacheRecipeData(final Context context, final ArrayList<Recipe> data) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(final Void... unused) {
                    try {

                        ContentValues[] recipeValues =
                                getRecipeContentValuesFromArray(context, data);
                        if ( recipeValues != null &&  recipeValues.length != 0) {
                /* Get a handle on the ContentResolver to delete and insert data */
                            ContentResolver movieContentResolver = context.getContentResolver();
                /* Delete old movies data */
//                movieContentResolver.delete(
//                        MovieEntry.CONTENT_URI,
//                        null,
//                        null);
                /* Insert our new movie data into Movie's ContentProvider */
                            movieContentResolver.bulkInsert(
                                    RecipeEntry.RECIPE_CONTENT_URI,
                                    recipeValues);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    return null;
                }
            }.execute();

        }

    public static ContentValues[] getIngredientsContentValuesFromArray(Context context, ArrayList<Recipe> recipes)
            throws JSONException {
        ArrayList<ContentValues> ingredientContentValues = new ArrayList<>();
        for (int i = 0; i < recipes.size(); i++) {
            Recipe data = recipes.get(i);
            ArrayList<Ingredient> ingredients = data.getIngredients();

            for(int j = 0; j < ingredients.size(); j++){

                Ingredient current = ingredients.get(j);

                ContentValues mIngredientValues = new ContentValues();
                mIngredientValues.put(RecipeEntry.COLUMN_RECIPE_ID, data.getId());
                mIngredientValues.put(RecipeEntry.COLUMN_INGRDIENT_QUANTITY, current.getQuantity());
                mIngredientValues.put(RecipeEntry.COLUMN_INGRDIENT_MEASURE, current.getMeasure());
                mIngredientValues.put(RecipeEntry.COLUMN_INGRDIENT_NAME, current.getIngredient());
                ingredientContentValues.add(mIngredientValues);
            }
        }
        ContentValues[] ingredientContentValue = new ContentValues[ingredientContentValues.size()];
        ingredientContentValue = ingredientContentValues.toArray(ingredientContentValue );
        return ingredientContentValue;
    }

        public static synchronized void cacheIngredientData(final Context context, final ArrayList<Recipe> data) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(final Void... unused) {
                    // Insert new task data via a ContentResolver
                    // Create new empty ContentValues object
                    try {
                        ContentValues[] ingredientValues =
                                getIngredientsContentValuesFromArray(context, data);
                        if ( ingredientValues != null &&  ingredientValues.length != 0) {
                /* Get a handle on the ContentResolver to delete and insert data */
                            ContentResolver ingredientContentResolver = context.getContentResolver();
                /* Delete old step data */
                ingredientContentResolver.delete(
                        RecipeEntry.INGREDIENTS_CONTENT_URI,
                        null,
                        null);
                /* Insert our new movie data into Movie's ContentProvider */
                            ingredientContentResolver.bulkInsert(
                                    RecipeEntry.INGREDIENTS_CONTENT_URI,
                                    ingredientValues);
                        }
                       // RecipeService.startAction(context);
                        // Display the URI that's returned with a Toast


                    } catch (Exception e) {
                        e.printStackTrace();
//                        Toast.makeText(context, "Error inserting data", Toast.LENGTH_LONG).show();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                }
            }.execute();

        }

    public static ContentValues[] getStepsContentValuesFromArray(Context context, ArrayList<Recipe> recipes)
            throws JSONException {
        ArrayList<ContentValues> stepContentValues = new ArrayList<>();

        for (int i = 0; i < recipes.size(); i++) {
            Recipe data = recipes.get(i);
            ArrayList<Step> steps = data.getSteps();

            for(int j = 0; j < steps.size(); j++){

                Step current = steps.get(j);

                ContentValues stepValues = new ContentValues();
                stepValues.put(RecipeEntry.COLUMN_RECIPE_ID, data.getId());
                stepValues.put(RecipeEntry.COLUMN_STEP_ID, current.getId());
                stepValues.put(RecipeEntry.COLUMN_STEP_SHORT_DESCRIPTION, current.getShortDescription());
                stepValues.put(RecipeEntry.COLUMN_STEP_DESCRIPTION, current.getDescription());
                stepValues.put(RecipeEntry.COLUMN_STEP_VIDEO_URL, current.getVideoURL());
                stepValues.put(RecipeEntry.COLUMN_STEP_THUMBNAIL, current.getThumbnailURL());
                stepContentValues.add(stepValues);
            }
        }
        ContentValues[] stepValues = new ContentValues[stepContentValues.size()];
        stepValues = stepContentValues.toArray(stepValues);
        return stepValues;
    }

    public static synchronized void cacheStepsData(final Context context, final ArrayList<Recipe> data) {
        new AsyncTask<Void, Void, Void>() {
            Uri uri = null;
            @Override
            protected Void doInBackground(final Void... unused) {
                // Insert new task data via a ContentResolver
                // Create new empty ContentValues object

                try {
                    ContentValues[] stepValues =
                            getStepsContentValuesFromArray(context, data);
                    if ( stepValues != null &&  stepValues.length != 0) {
                /* Get a handle on the ContentResolver to delete and insert data */
                        ContentResolver movieContentResolver = context.getContentResolver();
                /* Delete old step data */movieContentResolver.delete(
                        RecipeEntry.STEP_CONTENT_URI,
                        null,
                        null);
                /* Insert our new movie data into Movie's ContentProvider */
                        movieContentResolver.bulkInsert(
                                RecipeEntry.STEP_CONTENT_URI,
                                stepValues);
                    }
                    // Display the URI that's returned with a Toast


                } catch (Exception e) {
                    e.printStackTrace();
                    //Toast.makeText(context, "Error inserting data", Toast.LENGTH_LONG).show();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                RecipeService.startAction(context);
            }
        }.execute();

    }

    public static void cacheOfflineData(Context context, ArrayList<Recipe> recipes){
        cacheRecipeData(context, recipes);
        cacheIngredientData(context, recipes);
        cacheStepsData(context, recipes);
    }
    }


