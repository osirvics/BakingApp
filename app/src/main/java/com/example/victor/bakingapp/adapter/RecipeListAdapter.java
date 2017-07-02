package com.example.victor.bakingapp.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.victor.bakingapp.R;
import com.example.victor.bakingapp.StepListActivity;
import com.example.victor.bakingapp.model.Ingredient;
import com.example.victor.bakingapp.model.Recipe;
import com.example.victor.bakingapp.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder> {
    private ArrayList<Recipe> recipes;
    private Context context;

    public RecipeListAdapter(Context context, ArrayList<Recipe> recipes){
        this.context = context;
        this.recipes = recipes;
    }

    @Override
    public RecipeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_list_item, parent, false);
        return new RecipeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeListViewHolder holder, final int position) {
        final Recipe recipe = recipes.get(position);
        String recipeName = recipe.getName();
        int servings = recipe.getServings();
        holder.recipeName.setText(recipeName);
        holder.servings.setText(context.getString(R.string.servings, String.valueOf(servings)));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StepListActivity.class);
                if(null != recipe.getIngredients() && null != recipe.getSteps()){
                    ArrayList<Ingredient> ingredients = recipe.getIngredients();
                    ArrayList<Step> steps = recipe.getSteps();
                    intent.putParcelableArrayListExtra("ingredients", ingredients);
                    intent.putParcelableArrayListExtra("steps", steps);
                    intent.putExtra("name",recipe.getName());
                    intent.putExtra("pos", position);
                }
                intent.putExtra("id", recipe.getId());
                context.startActivity(intent);
//                ArrayList<Ingredient> ingredient = recipe.getIngredients();
//                for (Ingredient ingredient1: ingredient){
//                    Log.e("Adapter", "Ingreidents: " + ingredient1.getIngredient());
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return  (recipes == null) ? 0 : recipes.size();
    }

    public class RecipeListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recipe_name) TextView recipeName;
        @BindView(R.id.servings) TextView servings;
       public RecipeListViewHolder(View itemView) {
           super(itemView);
           ButterKnife.bind(this, itemView);
           //recipeName = (TextView) itemView.findViewById(R.id.recipe_name);
           //servings = (TextView)itemView.findViewById(R.id.servings);
       }
   }
}
