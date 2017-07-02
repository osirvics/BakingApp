package com.example.victor.bakingapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Victor on 29/06/2017.
 */

public class RecipeContract {
        /*
         * The "Content authority" is a name for the entire content provider, similar to the
         * relationship between a domain name and its website. A convenient string to use for the
         * content authority is the package name for the app, which is guaranteed to be unique on the
         * Play Store.
         */
        public static final String CONTENT_AUTHORITY = "com.example.victor.bakingapp";

        /*
         * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
         * the content provider for Sunshine.
         */
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

        public static final String PATH_RECIPE = "recipe";

        public static final String PATH_INGRDIENT = "recipe_ingredient";

        public static final String PATH_STEP = "recipe_step";

        /* Inner class that defines the table contents of the weather table */
        public static final class RecipeEntry implements BaseColumns {

            /* The base CONTENT_URI used to query the recipe table from the content provider */
            public static final Uri RECIPE_CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                    .appendPath(PATH_RECIPE)
                    .build();

            public static final Uri INGREDIENTS_CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                    .appendPath(PATH_INGRDIENT)
                    .build();

            public static final Uri STEP_CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                    .appendPath(PATH_STEP)
                    .build();

            /* Used internally as the name of our recipe tables  table. */
            public static final String RECIPE_TABLE_NAME = "recipe";

            public static final String INGRDIENT_TABLE_NAME = "ingredient";

            public static final String STEP_TABLE_NAME = "step";

             /* Varaible for recipe. */

            public static final String COLUMN_RECIPE_ID = "recipe_id";

            public static final String COLUMN_RECIPE_NAME = "recipe_name";

            public static final String COLUMN_RECIPE_SERVINGS = "servings";

            /* variables for ingredient. */

            public static final String COLUMN_INGRDIENT_QUANTITY= "ingredient_quantity";

            public static final String COLUMN_INGRDIENT_MEASURE = "ingredient_measure";

            public static final String COLUMN_INGRDIENT_NAME = "ingredient_name";

            /* variables for steps. */
            public static final String COLUMN_STEP_ID = "step_id";

            public static final String COLUMN_STEP_SHORT_DESCRIPTION = "step_short_description";

            public static final String COLUMN_STEP_DESCRIPTION = "step_description";

            public static final String COLUMN_STEP_VIDEO_URL = "step_video_url";

            public static final String COLUMN_STEP_THUMBNAIL = "step_thumnail";


        }

    }


