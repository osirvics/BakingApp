package com.example.victor.bakingapp.api;

import com.example.victor.bakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Victor on 26/06/2017.
 */

public interface BakingApiService {
        @GET("topher/2017/May/59121517_baking/baking.json")
        Call<List<Recipe>> getRecipes();
    }
