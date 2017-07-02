package com.example.victor.bakingapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.victor.bakingapp.data.RecipeContract.RecipeEntry;

public class RecipeProvider extends ContentProvider {

    public static final int CODE_RECIPE = 100;
    public static final int CODE_INGREDIENT = 200;
    public static final int CODE_STEPS = 300;
    public static final int CODE_RECIPE_WITH_ID = 101;
    public static final int CODE_INGRDIENT_WITH_ID = 201;
    public static final int CODE_STEPS_WITH_ID = 301;


    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private RecipeDbHelper mOpenHelper;


    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RecipeContract.CONTENT_AUTHORITY;


        /* This URI is content://com.example.android.popularmovies/recipe/ */
        matcher.addURI(authority, RecipeContract.PATH_RECIPE, CODE_RECIPE);
         /* This URI is content://com.example.android.popularmovies/ingredient/ */
        matcher.addURI(authority, RecipeContract.PATH_INGRDIENT, CODE_INGREDIENT);
           /* This URI is content://com.example.android.popularmovies/steps/ */
        matcher.addURI(authority, RecipeContract.PATH_STEP, CODE_STEPS);

        /*
         * This URI would look something like content://com.example.android.popularmovies/movie/14722
         * The "/#" signifies to the UriMatcher that if PATH_WEATHER is followed by ANY number,
         * that it should return the CODE_WEATHER_WITH_DATE code
         */
        matcher.addURI(authority, RecipeContract.PATH_RECIPE + "/#", CODE_RECIPE_WITH_ID);
        matcher.addURI(authority, RecipeContract.PATH_INGRDIENT + "/#", CODE_INGRDIENT_WITH_ID);
        matcher.addURI(authority, RecipeContract.PATH_STEP + "/#", CODE_STEPS_WITH_ID);

        return matcher;
    }
    @Override
    public boolean onCreate() {
        mOpenHelper = new RecipeDbHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case CODE_RECIPE: {
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
//                        long weatherDate =
//                                value.getAsLong(WeatherContract.WeatherEntry.COLUMN_DATE);
//                        if (!SunshineDateUtils.isDateNormalized(weatherDate)) {
//                            throw new IllegalArgumentException("Date must be normalized to insert");
//                        }

                        long _id = db.insert(RecipeEntry.RECIPE_TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            }

            case CODE_INGREDIENT: {
                db.beginTransaction();
                int rowsInsertedIntoTable = 0;
                try {
                    for (ContentValues value : values) {
//                        long weatherDate =
//                                value.getAsLong(WeatherContract.WeatherEntry.COLUMN_DATE);
//                        if (!SunshineDateUtils.isDateNormalized(weatherDate)) {
//                            throw new IllegalArgumentException("Date must be normalized to insert");
//                        }

                        long _id = db.insert(RecipeEntry.INGRDIENT_TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInsertedIntoTable++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInsertedIntoTable > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInsertedIntoTable;

            }

            case CODE_STEPS: {
                db.beginTransaction();
                int rowsSuccesfullyInserted = 0;
                try {
                    for (ContentValues value : values) {
//                        long weatherDate =
//                                value.getAsLong(WeatherContract.WeatherEntry.COLUMN_DATE);
//                        if (!SunshineDateUtils.isDateNormalized(weatherDate)) {
//                            throw new IllegalArgumentException("Date must be normalized to insert");
//                        }

                        long _id = db.insert(RecipeEntry.STEP_TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsSuccesfullyInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsSuccesfullyInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                Log.e("Database", "Rows inserted: " + rowsSuccesfullyInserted);
                return rowsSuccesfullyInserted;

            }

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;

        /*
         * Here's the switch statement that, given a URI, will determine what kind of request is
         * being made and query the database accordingly.
         */
        switch (sUriMatcher.match(uri)) {

            case CODE_RECIPE_WITH_ID: {

                String id = uri.getLastPathSegment();

                /*
                 * The query method accepts a string array of arguments, as there may be more
                 * than one "?" in the selection statement. Even though in our case, we only have
                 * one "?", we have to create a string array that only contains one element
                 * because this method signature accepts a string array.
                 */
                String[] selectionArguments = new String[]{id};

                cursor = mOpenHelper.getReadableDatabase().query(
                        /* Table we are going to query */
                        RecipeContract.RecipeEntry.RECIPE_TABLE_NAME,
                        /*
                         * A projection designates the columns we want returned in our Cursor.
                         * Passing null will return all columns of data within the Cursor.
                         * However, if you don't need all the data from the table, it's best
                         * practice to limit the columns returned in the Cursor with a projection.
                         */
                        projection,
                        RecipeEntry.COLUMN_RECIPE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }


            case CODE_STEPS_WITH_ID: {

                String id = uri.getLastPathSegment();

                /*
                 * The query method accepts a string array of arguments, as there may be more
                 * than one "?" in the selection statement. Even though in our case, we only have
                 * one "?", we have to create a string array that only contains one element
                 * because this method signature accepts a string array.
                 */
                String[] selectionArguments = new String[]{id};

                cursor = mOpenHelper.getReadableDatabase().query(
                        /* Table we are going to query */
                        RecipeContract.RecipeEntry.STEP_TABLE_NAME,
                        /*
                         * A projection designates the columns we want returned in our Cursor.
                         * Passing null will return all columns of data within the Cursor.
                         * However, if you don't need all the data from the table, it's best
                         * practice to limit the columns returned in the Cursor with a projection.
                         */
                        projection,
                        RecipeEntry.COLUMN_RECIPE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }


            case CODE_INGRDIENT_WITH_ID: {

                String id = uri.getLastPathSegment();

                /*
                 * The query method accepts a string array of arguments, as there may be more
                 * than one "?" in the selection statement. Even though in our case, we only have
                 * one "?", we have to create a string array that only contains one element
                 * because this method signature accepts a string array.
                 */
                String[] selectionArguments = new String[]{id};

                cursor = mOpenHelper.getReadableDatabase().query(
                        /* Table we are going to query */
                        RecipeEntry.INGRDIENT_TABLE_NAME,
                        /*
                         * A projection designates the columns we want returned in our Cursor.
                         * Passing null will return all columns of data within the Cursor.
                         * However, if you don't need all the data from the table, it's best
                         * practice to limit the columns returned in the Cursor with a projection.
                         */
                        projection,
                        RecipeEntry.COLUMN_RECIPE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_RECIPE: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        RecipeEntry.RECIPE_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_INGREDIENT: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        RecipeEntry.INGRDIENT_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case CODE_STEPS: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        RecipeEntry.STEP_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Get access to the task database (to write new data to)
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned

        switch (match) {
            case CODE_RECIPE:
                // Insert new values into the database
                // Inserting values into favorite table
                long id = db.insert(RecipeEntry.RECIPE_TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(RecipeEntry.RECIPE_CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
         /* Users of the delete method will expect the number of rows deleted to be returned. */
        int numRowsDeleted;

        /*
         * If we pass null as the selection to SQLiteDatabase#delete, our entire table will be
         * deleted. However, if we do pass null and delete all of the rows in the table, we won't
         * know how many rows were deleted. According to the documentation for SQLiteDatabase,
         * passing "1" for the selection will delete all rows and return the number of rows
         * deleted, which is what the caller of this method expects.
         */
        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case CODE_RECIPE:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        RecipeEntry.RECIPE_TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            case CODE_INGREDIENT:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        RecipeEntry.INGRDIENT_TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            case CODE_STEPS:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        RecipeEntry.STEP_TABLE_NAME,
                        selection,
                        selectionArgs);

                break;



            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numRowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
