package com.example.victor.bakingapp.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.victor.bakingapp.data.RecipeContract.RecipeEntry;


public class RecipeDbHelper extends SQLiteOpenHelper {
    /*
   * This is the name of our database. Database names should be descriptive and end with the
   * .db extension.
   */
    public static final String DATABASE_NAME = "recipe.db";

    private static final int DATABASE_VERSION = 1;

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /*
         * This String will contain a simple SQL statement that will create a table that will
         * cache recipe data.
         */
        final String SQL_CREATE_RECIPE_TABLE =

                "CREATE TABLE " + RecipeEntry.RECIPE_TABLE_NAME + " (" +

                /*
                 * RecipeEntry did not explicitly declare a column called "_ID". However,
                 * RecipeEntry implements the interface, "BaseColumns", which does have a field
                 * named "_ID". We use that here to designate our table's primary key.
                 */
                        RecipeEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        RecipeEntry.COLUMN_RECIPE_ID       + " INTEGER NOT NULL, "             +

                        RecipeEntry.COLUMN_RECIPE_NAME + " TEXT NOT NULL, "                     +

                        RecipeEntry.COLUMN_RECIPE_SERVINGS   + " INTEGER NOT NULL, "           +

                        " UNIQUE (" + RecipeEntry.COLUMN_RECIPE_ID + ") ON CONFLICT REPLACE);";


        //Second Table

        final String SQL_CREATE_INGREDIENT_TABLE =

                "CREATE TABLE " + RecipeEntry.INGRDIENT_TABLE_NAME + " (" +

                /*
                 * WeatherEntry did not explicitly declare a column called "_ID". However,
                 * WeatherEntry implements the interface, "BaseColumns", which does have a field
                 * named "_ID". We use that here to designate our table's primary key.
                 */
                        RecipeEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        RecipeEntry.COLUMN_RECIPE_ID       + " INTEGER NOT NULL, "             +

                        RecipeEntry.COLUMN_INGRDIENT_QUANTITY       + " REAL NOT NULL, "       +

                        RecipeEntry.COLUMN_INGRDIENT_MEASURE + " TEXT NOT NULL, "              +

                        RecipeEntry.COLUMN_INGRDIENT_NAME   + " TEXT NOT NULL " + ")";




        final String SQL_CREATE_STEP_TABLE =

                "CREATE TABLE " + RecipeEntry.STEP_TABLE_NAME  + " (" +

                /*
                 * WeatherEntry did not explicitly declare a column called "_ID". However,
                 * WeatherEntry implements the interface, "BaseColumns", which does have a field
                 * named "_ID". We use that here to designate our table's primary key.
                 */
                        RecipeEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        RecipeEntry.COLUMN_RECIPE_ID       + " INTEGER NOT NULL, "             +

                        RecipeEntry.COLUMN_STEP_ID       + " INTEGER NOT NULL, "               +

                        RecipeEntry.COLUMN_STEP_SHORT_DESCRIPTION + " TEXT NOT NULL, "          +

                        RecipeEntry.COLUMN_STEP_DESCRIPTION + " TEXT NOT NULL, "          +

                        RecipeEntry.COLUMN_STEP_VIDEO_URL   + " TEXT NOT NULL, "               +

                        RecipeEntry.COLUMN_STEP_THUMBNAIL  + " TEXT NOT NULL "  + ")";



        /*
         * After we've spelled out our SQLite table creation statement above, we actually execute
         * that SQL with the execSQL method of our SQLite database object.
         */
        sqLiteDatabase.execSQL(SQL_CREATE_RECIPE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_INGREDIENT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_STEP_TABLE);
    }

    /**
     * This database is only a cache for online data, so its upgrade policy is simply to discard
     * the data and call through to onCreate to recreate the table. Note that this only fires if
     * you change the version number for your database (in our case, DATABASE_VERSION). It does NOT
     * depend on the version number for your application found in your app/build.gradle file. If
     * you want to update the schema without wiping data, commenting out the current body of this
     * method should be your top priority before modifying this method.
     *
     * @param sqLiteDatabase Database that is being upgraded
     * @param oldVersion     The old database version
     * @param newVersion     The new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeEntry.RECIPE_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeEntry.INGRDIENT_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeEntry.STEP_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
