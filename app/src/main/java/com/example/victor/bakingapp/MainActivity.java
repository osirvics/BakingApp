package com.example.victor.bakingapp;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.victor.bakingapp.adapter.RecipeListAdapter;
import com.example.victor.bakingapp.api.BakingApiService;
import com.example.victor.bakingapp.api.BakingClient;
import com.example.victor.bakingapp.data.PersistData;
import com.example.victor.bakingapp.data.RecipeContract;
import com.example.victor.bakingapp.model.Ingredient;
import com.example.victor.bakingapp.model.Recipe;
import com.example.victor.bakingapp.widgets.RecipeService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements  LoaderManager.LoaderCallbacks<Cursor>  {
    private ArrayList<Recipe> recipes;
    private ArrayList<Ingredient> ingredients;
    private RecipeListAdapter adapter;
    private int favorite_recipe = 1;
    private static final int LOADER_ID_RECIPE = 34;
    @BindView(R.id.recipe_list_recyclerview) RecyclerView recipe_list_recyclerview;
    @BindView(R.id.empty) ProgressBar empty;
    @BindView(R.id.error_layout) LinearLayout errorLayout;
    @BindView(R.id.error_btn_retry) Button btnRetry;
    @BindView(R.id.error_txt_cause) TextView txtError;
    @Nullable
    private SimpleIdlingResource mIdlingResource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
        recipes = savedInstanceState.getParcelableArrayList("recipe");
        }
        ButterKnife.bind(this);
        getIdlingResource();
        Utils.getFavorieRecipe(this);
        displayData();
    }

    private void populateList() {
        adapter = new RecipeListAdapter(this, recipes);
        recipe_list_recyclerview.setAdapter(adapter);
        empty.setVisibility(View.GONE);

    }

    private void displayData() {
        if (recipes != null) {
            populateList();
        } else {
            if (mIdlingResource != null) {
                mIdlingResource.setIdleState(false);
            }
            BakingApiService apiService =
                    BakingClient.getClient().create(BakingApiService.class);
            Call<List<Recipe>> call = apiService.getRecipes();
            call.enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                    if (response.code() == 200) {
                        recipes = new ArrayList<>(response.body());
                        populateList();
                        if (mIdlingResource != null) {
                            mIdlingResource.setIdleState(true);
                        }
                        PersistData.cacheOfflineData(getApplicationContext(), recipes);
                        RecipeService.startAction(getApplicationContext());
                    }
                    else Log.d("MainActivity", "failed to get response");
                }

                @Override
                public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                   getSupportLoaderManager().initLoader(LOADER_ID_RECIPE, null, MainActivity.this);
                    //t.printStackTrace();
                    //showErrorView(t);
                }
            });
        }
    }

    private void showErrorView(Throwable throwable) {
        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
            txtError.setText(fetchErrorMessage(throwable));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item;
        if(favorite_recipe == Utils.NUTELA_PIE){
            item = menu.findItem(R.id.menu_favorite_nutella);
            item.setChecked(true);
        }
        else if(favorite_recipe == Utils.BROWNIES){
            item = menu.findItem(R.id.menu_favorite_brownies);
            item.setChecked(true);
        }
        else if(favorite_recipe == Utils.YELLOW_CAKE){
            item= menu.findItem(R.id.menu_favorite_cheesecake);
            item.setChecked(true);
        }
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_favorite_nutella:
                Utils.setFavoriteRecipe(this, Utils.NUTELA_PIE);
                RecipeService.startAction(this);
              checkMenu(item);
                break;
            case R.id.menu_favorite_brownies:
                Utils.setFavoriteRecipe(this,Utils.BROWNIES);
                RecipeService.startAction(this);
                checkMenu(item);
                break;
            case R.id.menu_favorite_yellow_Cake:
                Utils.setFavoriteRecipe(this, Utils.YELLOW_CAKE);
                RecipeService.startAction(this);
                checkMenu(item);
                break;
            case R.id.menu_favorite_cheesecake:
                Utils.setFavoriteRecipe(this, Utils.CHEESECAKE);
                RecipeService.startAction(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void checkMenu(MenuItem item){
        if (item.isChecked()) item.setChecked(false);
        else item.setChecked(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("recipe", recipes);
        super.onSaveInstanceState(outState);
    }

    /**
     * @param throwable to identify the type of error
     * @return appropriate error message
     */
    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }
        return errorMsg;
    }

    @OnClick(R.id.error_btn_retry)
    public void retry(){
        hideErrorView();
        displayData();
    }

    private void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        try {
//            return  new CursorLoader()getContentResolver().query(RecipeContract.RecipeEntry.RECIPE_CONTENT_URI,
//                    null,
//                    null,
//                    null, RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + " DESC");

            return new CursorLoader(this, RecipeContract.RecipeEntry.RECIPE_CONTENT_URI,
                    null,
                    null,
                    null, RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + " ASC");

        } catch (Exception e) {
            e.printStackTrace();
            showErrorView(new Throwable());
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.getCount()==0){
            showErrorView(new Throwable());
            return;
        }
        Log.e("MainActivity", "Size of cursor: " + data.getCount());
        recipes = new ArrayList<>();
        recipes.clear();
        if(data != null && data.moveToFirst() ){
            int mMovieIdIndex = data.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID);
            int mMovieNameIndex = data.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME);
            int mMovieServingsIndex = data.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_SERVINGS);
            do{
                Recipe recipe = new Recipe();
                recipe.setId(data.getInt(mMovieIdIndex));
                recipe.setName(data.getString(mMovieNameIndex));
                recipe.setServings(data.getInt(mMovieServingsIndex));
                recipes.add(recipe);
            }
            while (data.moveToNext());
        }

        populateList();
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
