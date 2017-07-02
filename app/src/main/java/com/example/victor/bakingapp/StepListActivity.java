package com.example.victor.bakingapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.example.victor.bakingapp.adapter.StepListAdapter;
import com.example.victor.bakingapp.data.RecipeContract;
import com.example.victor.bakingapp.interfaces.OnItemClickListener;
import com.example.victor.bakingapp.model.Ingredient;
import com.example.victor.bakingapp.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepListActivity extends AppCompatActivity implements OnItemClickListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    ArrayList<Ingredient> ingredients;
    ArrayList<Step> steps;
    private StepListAdapter adapter;
    @BindView(R.id.ingredients) TextView ingredient_list;
    @BindView(R.id.item_list) RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


       // toolbar.setTitle(getTitle());

        Intent intent = getIntent();
        if (intent.hasExtra("ingredients") && intent.hasExtra("steps")) {
            getSupportActionBar().setTitle(intent.getStringExtra("name"));
            ingredients = new ArrayList<>();
            steps = new ArrayList<>();
            ingredients = intent.getParcelableArrayListExtra("ingredients");
            steps = intent.getParcelableArrayListExtra("steps");
            setupRecyclerView(recyclerView);
            populateIngredients(ingredient_list, ingredients);
        }
        else{
            if(intent.hasExtra("id")){
                loadOfflineData(getIntent().getIntExtra("id",0));
                //insertSquawk(2);
            }

        }

//        if (findViewById(R.id.item_detail_container) != null) {
//            mTwoPane = true;
//        }
    }

    private void populateIngredients(TextView textView, ArrayList<Ingredient> ingredients){
        for (Ingredient ingredient: ingredients){
            float quantity;
            String measure;
            String actualIngredient;
            quantity = ingredient.getQuantity();
            measure = ingredient.getMeasure();
            actualIngredient = ingredient.getIngredient();
            String concat = String.valueOf(quantity) + " " + measure;
            String joined = getString(R.string.preposition, concat);
            String fullDescription = "> " + joined + " " + actualIngredient +"\n";
            textView.append(fullDescription);

        }

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        //recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
        adapter = new StepListAdapter(this, steps, this);
        recyclerView.setAdapter(adapter);

        final int columns = getResources().getInteger(R.integer.gallery_columns);
        final GridLayoutManager gridLayoutManager =
                new GridLayoutManager(this, columns);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        int spacing =  getResources().getDimensionPixelSize(R.dimen.rc_padding_left);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), spacing);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void OnItemClick(int position) {
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            Bundle arguments = new Bundle();
            arguments.putParcelableArrayList("data", steps);
            arguments.putInt("pos", position);
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putParcelableArrayListExtra("data",steps);
            intent.putExtra("pos", position);
            startActivity(intent);
        }
    }

    private void loadOfflineData(final int id) {
        // Database operations should not be done on the main thread
        final AsyncTask<Void, Void, Void> insertSquawkTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                String rowId = String.valueOf(id);
                String selectionClause = RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + " LIKE ?";
                String[] selectionArgs = {rowId};
                try {


                    Cursor mIngredientcursor = getContentResolver().query(RecipeContract.RecipeEntry.INGREDIENTS_CONTENT_URI,
                            null,
                            selectionClause,
                            selectionArgs, RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + " ASC");
                    ingredients = new ArrayList<>();

                    if(mIngredientcursor != null && mIngredientcursor.moveToFirst()){
                        int quantityIndex = mIngredientcursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_INGRDIENT_QUANTITY);
                        int measureIndex = mIngredientcursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_INGRDIENT_MEASURE);
                        int ingrdientIndex = mIngredientcursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_INGRDIENT_NAME);
                        do{
                            Ingredient ingredient = new Ingredient();
                            ingredient.setQuantity(mIngredientcursor.getLong(quantityIndex));
                            ingredient.setMeasure(mIngredientcursor.getString(measureIndex));
                            ingredient.setIngredient(mIngredientcursor.getString(ingrdientIndex));
                            ingredients.add(ingredient);
                        }
                     while (mIngredientcursor.moveToNext());
                        mIngredientcursor.close();
                        mIngredientcursor =null;


                        Cursor mStepCursor = getContentResolver().query(RecipeContract.RecipeEntry.STEP_CONTENT_URI,
                                null,
                                selectionClause,
                                selectionArgs, RecipeContract.RecipeEntry.COLUMN_STEP_ID + " ASC");

                        Log.e("ItemListActivity", "Size of Step cursor: " + mStepCursor.getCount());

                        steps = new ArrayList<>();

                        if(mStepCursor != null && mStepCursor.moveToFirst()){
                            int stepIdIndex = mStepCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_STEP_ID);
                            int shortDescriptionIndex = mStepCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_STEP_SHORT_DESCRIPTION);
                            int stepDescripion = mStepCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_STEP_DESCRIPTION);
                            int stepVideoUrl = mStepCursor.getColumnIndexOrThrow(RecipeContract.RecipeEntry.COLUMN_STEP_VIDEO_URL);
                            int stepThumbnail = mStepCursor.getColumnIndexOrThrow(RecipeContract.RecipeEntry.COLUMN_STEP_THUMBNAIL);
                            do{
                               Step step = new Step();
                                step.setId(mStepCursor.getInt(stepIdIndex));
                                step.setShortDescription(mStepCursor.getString(shortDescriptionIndex));
                                step.setDescription(mStepCursor.getString(stepDescripion));
                                if (null != mStepCursor.getString(stepVideoUrl))
                                step.setVideoURL(mStepCursor.getString(stepVideoUrl));
                                if (null != mStepCursor.getString(stepThumbnail))
                                    step.setThumbnailURL(mStepCursor.getString(stepThumbnail));
                                steps.add(step);
                            }
                            while (mStepCursor.moveToNext());
                        }

                    }

                  //  Log.e("ItemListActivity", "Size of ingredient cursor: " + mIngredientcursor.getCount());


                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                setupRecyclerView(recyclerView);
                populateIngredients(ingredient_list, ingredients);
               /* AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(), RecipeAppWidget.class));
                RecipeAppWidget.updateRecipeWidgets(getApplicationContext(),appWidgetManager,appWidgetIds,ingredients,"Chesse Cake");*/
            }
        };

        insertSquawkTask.execute();
    }
}
