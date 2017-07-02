package com.example.victor.bakingapp;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Victor on 01/07/2017.
 */

public class Utils {
    public static final String RECIPE = "recipe";
    public static final int NUTELA_PIE = 1;
    public static final int BROWNIES = 2;
    public static final int YELLOW_CAKE= 3;
    public static final int CHEESECAKE =4;

    public static String  getRecipeName(int id, Context context){
        if(id == NUTELA_PIE){
            return context.getString(R.string.menu_nutella);
        }
        else if (id == BROWNIES){
            return context.getString(R.string.menu_brownies);
        }
        else if(id == YELLOW_CAKE){
            return context.getString(R.string.menu_yellow_cake);
        }
        else{
            return context.getString(R.string.menu_cheesecake);
        }
    }



    public static int getFavorieRecipe(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(RECIPE, NUTELA_PIE);
    }
    public static void setFavoriteRecipe(Context context, int input) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(RECIPE, input)
                .apply();
    }
}
